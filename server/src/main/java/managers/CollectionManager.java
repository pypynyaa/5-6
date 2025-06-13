package managers;

import mainClasses.HumanBeing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CollectionManager {
    private LinkedHashSet<HumanBeing> humanCollection;
    private final LocalDate creationDate;

    public CollectionManager() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("props.txt");

            if (inputStream == null) {
                System.err.println("Файл 'props.txt' не найден в classpath. Попытка чтения по относительному пути...");
                inputStream = Files.newInputStream(Paths.get("server/src/main/resources/props.txt"));
            }

            if (inputStream == null) {
                throw new IOException("Файл 'props.txt' не найден ни в classpath, ни по относительному пути.");
            }

            List<String> lines;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                lines = reader.lines().collect(Collectors.toList());
            }

            String sshUser = lines.get(0);
            String sshPassword = lines.get(1);
            String sshHost = lines.get(2);
            int sshPort = Integer.parseInt(lines.get(3));
            int localPort = Integer.parseInt(lines.get(4));
            String dbHost = lines.get(5);
            int dbPort = Integer.parseInt(lines.get(6));
            String dbName = lines.get(7);
            String dbUser = sshUser;
            String dbPassword = lines.get(8);

            DatabaseManager.initialize(sshUser, sshPassword, sshHost, sshPort, localPort, dbHost, dbPort, dbName, dbUser, dbPassword);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла props.txt: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить параметры подключения к БД из файла.", e);
        } catch (SQLException e1) {
            System.err.println("Ошибка инициализации DatabaseManager: " + e1.getMessage());
            e1.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать подключение к БД.", e1);
        }

        this.humanCollection = new LinkedHashSet<>();
        creationDate = LocalDate.now();
        loadCollectionFromDatabase();
    }


    public String loadCollectionFromDatabase() {
        this.humanCollection = DatabaseManager.loadAllHumanBeings();
        return "Коллекция успешно загружена из базы данных. Загружено элементов: " + humanCollection.size();
    }


    public void saveCollectionToDatabase() {
        System.out.println("Метод saveCollectionToDatabase пока не реализован. Все изменения сохраняются в БД немедленно.");
    }

    public void saveCollectionToFile() throws IOException, SQLException {
        System.out.println("Коллекция сохраняется в базу данных. Сохранение в файл в этой версии не предусмотрено.");
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }


    public LinkedHashSet<HumanBeing> getHumansCollection() {
        return humanCollection;
    }


    public void setHumansCollection(LinkedHashSet<HumanBeing> humanCollection) {
        this.humanCollection = humanCollection;
    }


    public String getCollectionInfo() {
        String collectionType = humanCollection.getClass().getSimpleName();
        return ("Тип коллекции - " + collectionType + "\nДата создания - " + getCreationDate() + "\nКоличество элементов - " + humanCollection.size());
    }


    public String showCollectionElements(int page, int pageSize) {
        if (humanCollection.isEmpty()) {
            return "Коллекция пуста.";
        }
        if (page < 1 || pageSize < 1) {
            return "Некорректные параметры страницы или размера страницы. Номер страницы и размер страницы должны быть положительными числами (>= 1).";
        }

        List<HumanBeing> sortedHumans = humanCollection.stream().sorted(Comparator.comparing(HumanBeing::getId)).collect(Collectors.toList());

        return formatHumansTable(sortedHumans, page, pageSize);
    }


    public String formatHumansTable(List<HumanBeing> humans, int page, int pageSize) {
        if (humans.isEmpty()) {
            return "Коллекция пуста.";
        }

        int totalElements = humans.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        if (page > totalPages && totalElements > 0) {
            return "Запрошена страница " + page + ", но доступно только " + totalPages + " страниц. Всего элементов: " + totalElements + ".";
        }

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalElements);

        if (startIndex < 0 || startIndex > totalElements && totalElements > 0) {
            System.err.println("Внутренняя ошибка: Некорректный startIndex: " + startIndex + ", totalElements: " + totalElements);
            return "Внутренняя ошибка при расчете диапазона элементов. Пожалуйста, сообщите администратору.";
        }
        List<HumanBeing> pageElements;
        try {
            pageElements = humans.subList(startIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("CRITICAL ERROR: IndexOutOfBoundsException в subList: " + e.getMessage());
            System.err.println("  Параметры: page=" + page + ", pageSize=" + pageSize + ", totalElements=" + totalElements + ", startIndex=" + startIndex + ", endIndex=" + endIndex);
            e.printStackTrace();
            return "Внутренняя ошибка сервера при получении элементов страницы. Пожалуйста, сообщите администратору.";
        }

        StringBuilder sb = new StringBuilder();
        String[] headers = HumanBeing.getTableHeaders();
        List<String[]> rows = pageElements.stream().map(HumanBeing::toTableRow).collect(Collectors.toList());

        int[] minWidths = {4, 4, 8, 8, 5, 5, 5, 8, 5, 5, 5};
        if (minWidths.length != headers.length) {
            System.err.println("WARNING: minWidths array length (" + minWidths.length + ") does not match headers length (" + headers.length + "). Using dynamic minimums.");
            minWidths = new int[headers.length];
            Arrays.fill(minWidths, 3);
        }

        int[] widths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            widths[i] = Math.max(headers[i].length(), minWidths[i]);
            for (String[] row : rows) {
                if (i < row.length && row[i] != null) {
                    widths[i] = Math.max(widths[i], row[i].length());
                }
            }
            widths[i] += 2;
        }

        String formatLine = "+" + Arrays.stream(widths).mapToObj(w -> "-".repeat(w)).collect(Collectors.joining("+")) + "+\n";

        String formatHeader = "|" + IntStream.range(0, headers.length).mapToObj(i -> centerString(headers[i], widths[i])).collect(Collectors.joining("|")) + "|\n";

        sb.append(formatLine).append(formatHeader).append(formatLine);

        rows.forEach(row -> {
            String formatRow = "|" + IntStream.range(0, headers.length).mapToObj(i -> {
                String cellContent = (i < row.length && row[i] != null) ? row[i] : "-";
                return centerString(cellContent, widths[i]);
            }).collect(Collectors.joining("|")) + "|\n";
            sb.append(formatRow);
        });

        sb.append(formatLine);
        sb.append("Страница ").append(page).append(" из ").append(totalPages).append(". Всего элементов: ").append(totalElements).append(".\n");

        return sb.toString();
    }

    private static String centerString(String s, int width) {
        if (s == null) s = "";
        if (width <= s.length()) {
            return s;
        }
        int padding = width - s.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + s + " ".repeat(padRight);
    }


    public String addElement(HumanBeing humanBeing, String username) {
        if (humanBeing == null) {
            return "HumanBeing не может быть null.";
        }


        humanBeing.setCreationDate(LocalDate.now());
        humanBeing.setUser(username);


        try {


            humanBeing.validate();
        } catch (IllegalArgumentException e) {
            return "Ошибка валидации HumanBeing: " + e.getMessage();
        }

        try {
            int generatedId = DatabaseManager.addHumanBeing(humanBeing);
            if (generatedId != -1) {
                humanBeing.setId(generatedId);
                humanCollection.add(humanBeing);


                loadCollectionFromDatabase();
                return "Элемент успешно добавлен с ID: " + humanBeing.getId();
            } else {
                return "Ошибка при добавлении HumanBeing в базу данных. (Возможно, дубликат или другая ошибка БД)";
            }
        } catch (SQLException e) {
            System.err.println("SQLException при добавлении HumanBeing: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка базы данных при добавлении HumanBeing: " + e.getMessage();
        }
    }


    public String updateElement(int id, HumanBeing updatedHumanBeing, String username) {
        if (updatedHumanBeing == null) {
            return "Новый объект HumanBeing не может быть null.";
        }

        HumanBeing existingHuman = findHumanById(id);

        if (existingHuman == null) {
            return "HumanBeing с ID " + id + " нет в коллекции.";
        }

        if (!existingHuman.getUser().equals(username)) {
            return "У пользователя " + username + " нет прав на обновление данных HumanBeing с ID " + id;
        }

        try {
            updatedHumanBeing.validate();
            updatedHumanBeing.setId(id);
            updatedHumanBeing.setUser(existingHuman.getUser());
            updatedHumanBeing.setCreationDate(existingHuman.getCreationDate());
        } catch (IllegalArgumentException e) {
            return "Ошибка валидации обновляемого элемента: " + e.getMessage();
        }

        try {
            if (DatabaseManager.updateHumanBeing(id, updatedHumanBeing)) {
                humanCollection.removeIf(h -> h.getId() != null && h.getId().equals(id));
                humanCollection.add(updatedHumanBeing);

                return "Данные о HumanBeing с ID " + id + " обновлены успешно!";
            } else {
                return "Ошибка обновления данных о HumanBeing с ID " + id + ". Возможно, HumanBeing не найден в БД или произошла другая ошибка.";
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении данных HumanBeing в БД: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка базы данных при обновлении HumanBeing: " + e.getMessage();
        }
    }


    public String removeElement(int id, String username) {
        HumanBeing humanToDelete = findHumanById(id);

        if (humanToDelete == null) {
            return "Элемент с ID " + id + " не найден для удаления.";
        }

        if (!humanToDelete.getUser().equals(username)) {
            return "У пользователя " + username + " нет прав на удаление HumanBeing с ID " + id;
        }

        try {
            if (DatabaseManager.deleteHumanBeing(humanToDelete)) {
                humanCollection.removeIf(human -> human.getId() != null && human.getId().equals(id));
                return "Элемент с ID " + id + " успешно удален.";
            } else {
                return "Элемент с ID " + id + " не был удален из базы данных.";
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении HumanBeing из БД: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка базы данных при удалении HumanBeing: " + e.getMessage();
        }
    }


    public String removeBySoundtrackName(String soundtrackName, String username) {
        if (soundtrackName == null) {
            return "Название саундтрека не может быть null.";
        }

        List<HumanBeing> humansToRemove = humanCollection.stream().filter(human -> human.getSoundtrackName() != null && human.getSoundtrackName().equals(soundtrackName) && human.getUser() != null && human.getUser().equals(username)).collect(Collectors.toList());

        if (humansToRemove.isEmpty()) {
            return "Не найдено элементов, принадлежащих вам, с названием саундтрека '" + soundtrackName + "' для удаления.";
        }

        int removedCount = 0;
        StringBuilder result = new StringBuilder();
        for (HumanBeing human : humansToRemove) {
            try {
                if (DatabaseManager.deleteHumanBeing(human)) {
                    humanCollection.remove(human);
                    removedCount++;
                    result.append("Элемент с ID ").append(human.getId()).append(" ('").append(human.getName()).append("') удален.\n");
                } else {
                    result.append("Ошибка удаления элемента с ID ").append(human.getId()).append(" ('").append(human.getName()).append("') из БД.\n");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД при удалении HumanBeing по саундтреку: " + e.getMessage());
                e.printStackTrace();
                result.append("Ошибка БД при удалении элемента с ID ").append(human.getId()).append(": ").append(e.getMessage()).append("\n");
            }
        }
        result.insert(0, "Удалено " + removedCount + " элементов с названием саундтрека '" + soundtrackName + "'.\n");
        return result.toString();
    }


    public String removeGreater(HumanBeing humanBeingToCompare, String username) {
        if (humanBeingToCompare == null) {
            return "Объект HumanBeing для сравнения не может быть null.";
        }
        if (username == null || username.isEmpty()) {
            return "Имя пользователя не может быть пустым.";
        }

        List<HumanBeing> humansToRemove = humanCollection.stream().filter(human -> human.getUser() != null && human.getUser().equals(username) && human.compareTo(humanBeingToCompare) > 0).collect(Collectors.toList());

        if (humansToRemove.isEmpty()) {
            return "Нет элементов, принадлежащих вам, которые были бы больше заданного HumanBeing.";
        }

        int removedCount = 0;
        StringBuilder result = new StringBuilder();
        for (HumanBeing human : humansToRemove) {
            try {
                if (DatabaseManager.deleteHumanBeing(human)) {
                    humanCollection.remove(human);
                    removedCount++;
                    result.append("Элемент '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") удален.\n");
                } else {
                    result.append("Ошибка удаления элемента '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") из БД.\n");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД при удалении HumanBeing в removeGreater: " + e.getMessage());
                e.printStackTrace();
                result.append("Ошибка БД при удалении элемента '").append(human.getName()).append("' (ID: ").append(human.getId()).append("): ").append(e.getMessage()).append("\n");
            }
        }
        result.insert(0, "Удалено " + removedCount + " элементов, превышающих заданный HumanBeing.\n");
        return result.toString();
    }


    public String removeLower(HumanBeing humanBeingToCompare, String username) {
        if (humanBeingToCompare == null) {
            return "Объект HumanBeing для сравнения не может быть null.";
        }
        if (username == null || username.isEmpty()) {
            return "Имя пользователя не может быть пустым.";
        }

        List<HumanBeing> humansToRemove = humanCollection.stream().filter(human -> human.getUser() != null && human.getUser().equals(username) && human.compareTo(humanBeingToCompare) < 0).collect(Collectors.toList());

        if (humansToRemove.isEmpty()) {
            return "Нет элементов, принадлежащих вам, которые были бы меньше заданного HumanBeing.";
        }

        int removedCount = 0;
        StringBuilder result = new StringBuilder();
        for (HumanBeing human : humansToRemove) {
            try {
                if (DatabaseManager.deleteHumanBeing(human)) {
                    humanCollection.remove(human);
                    removedCount++;
                    result.append("Элемент '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") удален.\n");
                } else {
                    result.append("Ошибка удаления элемента '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") из БД.\n");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД при удалении HumanBeing в removeLower: " + e.getMessage());
                e.printStackTrace();
                result.append("Ошибка БД при удалении элемента '").append(human.getName()).append("' (ID: ").append(human.getId()).append("): ").append(e.getMessage()).append("\n");
            }
        }
        result.insert(0, "Удалено " + removedCount + " элементов, меньших заданного HumanBeing.\n");
        return result.toString();
    }


    public String clearCollection(String username) {
        StringBuilder res = new StringBuilder();
        res.append("Попытка удаления элементов пользователя '").append(username).append("':\n");

        List<HumanBeing> humansToRemove = humanCollection.stream().filter(human -> human.getUser() != null && human.getUser().equals(username)).collect(Collectors.toList());

        if (humansToRemove.isEmpty()) {
            return "Нет элементов, принадлежащих пользователю '" + username + "', для удаления.";
        }

        int removedCount = 0;
        for (HumanBeing human : humansToRemove) {
            try {
                if (DatabaseManager.deleteHumanBeing(human)) {
                    humanCollection.remove(human);
                    res.append("HumanBeing '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") - удален.\n");
                    removedCount++;
                } else {
                    res.append("HumanBeing '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") - не удален из БД.\n");
                }
            } catch (SQLException e) {
                res.append("Ошибка во время удаления HumanBeing '").append(human.getName()).append("' (ID: ").append(human.getId()).append(") из БД: ").append(e.getMessage()).append("\n");
                System.err.println("SQLException при удалении HumanBeing во время clear: " + e.getMessage());
                e.printStackTrace();
            }
        }
        res.append("Всего удалено элементов: ").append(removedCount).append(".\n");
        return res.toString();
    }


    public HumanBeing findHumanById(int id) {
        return humanCollection.stream().filter(human -> human.getId() != null && human.getId().equals(id)).findFirst().orElse(null);
    }


    public String filterLessThanSoundtrackName(String soundtrackName) {
        if (humanCollection.isEmpty()) {
            return "Коллекция пуста.";
        }
        if (soundtrackName == null) {
            return "Название саундтрека для сравнения не может быть null.";
        }

        List<HumanBeing> filteredHumans = humanCollection.stream().filter(h -> h.getSoundtrackName() != null && h.getSoundtrackName().compareTo(soundtrackName) < 0).sorted(Comparator.comparing(HumanBeing::getId)).collect(Collectors.toList());

        if (filteredHumans.isEmpty()) {
            return "Нет элементов с названием саундтрека меньше '" + soundtrackName + "'.";
        }

        StringBuilder sb = new StringBuilder("Элементы, у которых soundtrackName меньше '" + soundtrackName + "':\n");
        sb.append(formatHumansTable(filteredHumans, 1, filteredHumans.size()));
        return sb.toString();
    }


    public String printDescending(int page, int pageSize) {
        if (humanCollection.isEmpty()) {
            return "Коллекция пуста.";
        }
        if (page < 1 || pageSize < 1) {
            return "Некорректные параметры страницы или размера страницы. Номер страницы и размер страницы должны быть положительными числами (>= 1).";
        }

        List<HumanBeing> sortedHumans = humanCollection.stream().sorted(Comparator.comparing(HumanBeing::getId).reversed()).collect(Collectors.toList());

        return formatHumansTable(sortedHumans, page, pageSize);
    }


    public String minByImpactSpeed() {
        if (humanCollection.isEmpty()) {
            return "Коллекция пуста.";
        }
        HumanBeing minImpactSpeedHuman = humanCollection.stream().min(Comparator.comparingDouble(HumanBeing::getImpactSpeed)).orElse(null);

        return minImpactSpeedHuman != null ? minImpactSpeedHuman.toString() : "Элементы с минимальной скоростью удара не найдены.";
    }

}