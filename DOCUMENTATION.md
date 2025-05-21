# Документация по системе управления коллекцией HumanBeing

## Общее описание
Система представляет собой консольное приложение для управления коллекцией объектов типа HumanBeing. Коллекция хранится в XML-файле и может быть загружена/сохранена. Поддерживаются различные команды для работы с коллекцией.

## Основные классы

### 1. HumanBeing
Основной класс, представляющий объект коллекции. Реализует интерфейс `Comparable<HumanBeing>` для сравнения объектов по имени.

#### Подробное описание полей:
1. `id` (Long):
   - Уникальный идентификатор объекта
   - Генерируется автоматически при создании
   - Не может быть null
   - Должен быть положительным числом

2. `name` (String):
   - Имя человека
   - Не может быть null
   - Не может быть пустой строкой
   - Должно содержать только допустимые символы

3. `coordinates` (Coordinates):
   - Объект класса Coordinates
   - Содержит координаты x и y
   - Не может быть null
   - X должно быть больше -123
   - Y не может быть null

4. `creationDate` (Date):
   - Дата создания объекта
   - Генерируется автоматически
   - Не может быть null
   - Формат: yyyy-MM-dd HH:mm:ss

5. `realHero` (boolean):
   - Является ли человек реальным героем
   - Не может быть null
   - По умолчанию false

6. `hasToothpick` (Boolean):
   - Есть ли у человека зубочистка
   - Может быть null
   - По умолчанию null

7. `impactSpeed` (Float):
   - Скорость удара
   - Может быть null
   - Если не null, должно быть больше -123

8. `soundtrackName` (String):
   - Название саундтрека
   - Не может быть null
   - Не может быть пустой строкой

9. `minutesOfWaiting` (Long):
   - Минуты ожидания
   - Может быть null
   - Если не null, должно быть положительным числом

10. `weaponType` (WeaponType):
    - Тип оружия
    - Может быть null
    - Должен быть одним из допустимых значений enum

11. `car` (Car):
    - Объект класса Car
    - Может быть null
    - Если не null, должен быть валидным

#### Подробное описание методов:
1. Конструкторы:
   - Конструктор по умолчанию
   - Конструктор с параметрами для всех полей
   - Конструктор для копирования объекта

2. Геттеры и сеттеры:
   - Для каждого поля есть соответствующий геттер и сеттер
   - Сеттеры выполняют базовую валидацию
   - Геттеры могут возвращать null для nullable полей

3. `toString()`:
   - Возвращает строковое представление объекта
   - Формат: "HumanBeing{id=1, name='John', ...}"
   - Включает все поля объекта

4. `compareTo()`:
   - Сравнивает объекты по имени
   - Используется для сортировки
   - Учитывает регистр букв

### 2. HumanBeingCollection
Класс для управления коллекцией HumanBeing. Использует `TreeSet` для хранения объектов, что обеспечивает автоматическую сортировку.

#### Подробное описание методов:
1. `addToCollection(HumanBeing humanBeing)`:
   - Добавляет объект в коллекцию
   - Проверяет валидность объекта
   - Генерирует новый ID, если необходимо
   - Возвращает true при успешном добавлении

2. `removeFromCollection(HumanBeing humanBeing)`:
   - Удаляет объект из коллекции
   - Проверяет существование объекта
   - Возвращает true при успешном удалении

3. `clearCollection()`:
   - Удаляет все объекты из коллекции
   - Освобождает память
   - Сбрасывает счетчик ID

4. `getHumanBeingSet()`:
   - Возвращает копию множества объектов
   - Защищает оригинальную коллекцию от изменений
   - Возвращает пустое множество, если коллекция пуста

5. `size()`:
   - Возвращает количество объектов в коллекции
   - Возвращает 0 для пустой коллекции

6. `getMaxId()`:
   - Возвращает максимальный ID в коллекции
   - Возвращает 0 для пустой коллекции
   - Используется для генерации новых ID

7. `getById(Long id)`:
   - Ищет объект по ID
   - Возвращает null, если объект не найден
   - Использует stream API для поиска

8. `getAll()`:
   - Возвращает список всех объектов
   - Сортирует объекты по имени
   - Возвращает пустой список для пустой коллекции

9. `removeById(Long id)`:
   - Удаляет объект по ID
   - Возвращает true при успешном удалении
   - Возвращает false, если объект не найден

10. `removeGreater(HumanBeing humanBeing)`:
    - Удаляет все объекты, большие заданного
    - Использует compareTo для сравнения
    - Возвращает количество удаленных объектов

11. `removeLower(HumanBeing humanBeing)`:
    - Удаляет все объекты, меньшие заданного
    - Использует compareTo для сравнения
    - Возвращает количество удаленных объектов

12. `removeAllByMinutesOfWaiting(Long minutesOfWaiting)`:
    - Удаляет все объекты с заданным значением minutesOfWaiting
    - Возвращает количество удаленных объектов
    - Игнорирует null значения

13. `filterBySoundtrackName(String soundtrackName)`:
    - Фильтрует объекты по названию саундтрека
    - Возвращает список подходящих объектов
    - Учитывает регистр букв

14. `printFieldDescendingWeaponType()`:
    - Выводит типы оружия по убыванию
    - Игнорирует null значения
    - Использует stream API для сортировки

### 3. CommandManager
Менеджер команд для обработки пользовательского ввода. Использует паттерн Command для реализации команд.

#### Подробное описание методов:
1. `registerCommand(String name, Command command)`:
   - Регистрирует новую команду
   - Проверяет уникальность имени
   - Сохраняет команду в HashMap
   - Возвращает true при успешной регистрации

2. `executeCommand(String name, String args, Printer printer)`:
   - Выполняет команду по имени
   - Передает аргументы команде
   - Обрабатывает исключения
   - Возвращает true при успешном выполнении

3. `printHelp()`:
   - Выводит справку по всем командам
   - Сортирует команды по имени
   - Форматирует вывод для удобства чтения

### 4. UserManager
Класс для взаимодействия с пользователем. Реализует основной цикл программы.

#### Подробное описание методов:
1. `start()`:
   - Запускает программу
   - Загружает коллекцию из файла
   - Регистрирует команды
   - Запускает основной цикл

2. `readLine()`:
   - Читает ввод пользователя
   - Обрабатывает Ctrl+D
   - Возвращает введенную строку
   - Возвращает "exit" при нажатии Ctrl+D

3. `registerCommands()`:
   - Регистрирует все доступные команды
   - Создает экземпляры команд
   - Передает необходимые зависимости

### 5. XMLReader и XMLWriter
Классы для работы с XML-файлами. Используют JAXB для сериализации/десериализации.

#### XMLReader:
1. `readFromFile()`:
   - Читает XML-файл
   - Парсит XML в объекты
   - Валидирует структуру XML
   - Возвращает Map с данными

#### XMLWriter:
1. `writeToFile()`:
   - Записывает объекты в XML-файл
   - Форматирует XML для читаемости
   - Обрабатывает ошибки записи

### 6. XMLSerializer
Класс для сериализации/десериализации коллекции. Является фасадом для JAXB.

#### Подробное описание методов:
1. `saveToFile(HumanBeingCollection collection, File file)`:
   - Сериализует коллекцию в XML
   - Проверяет валидность данных
   - Обрабатывает ошибки сериализации
   - Возвращает true при успешном сохранении

2. `loadFromFile(File file)`:
   - Десериализует XML в коллекцию
   - Валидирует структуру XML
   - Обрабатывает ошибки десериализации
   - Возвращает коллекцию объектов

3. `getValue(String[] key, LinkedHashMap<String[], String> map)`:
   - Ищет значение по ключу в Map
   - Обрабатывает отсутствующие ключи
   - Возвращает null, если ключ не найден

4. `arrayEquals(String[] arr1, String[] arr2)`:
   - Сравнивает массивы строк
   - Учитывает порядок элементов
   - Возвращает true при полном совпадении

### 7. Printer
Класс для вывода информации. Обеспечивает единообразный вывод во всей программе.

#### Подробное описание методов:
1. `println(String message)`:
   - Выводит сообщение с переносом строки
   - Форматирует сообщение
   - Обрабатывает null значения

2. `print(String message)`:
   - Выводит сообщение без переноса строки
   - Форматирует сообщение
   - Обрабатывает null значения

3. `printError(String message)`:
   - Выводит сообщение об ошибке
   - Использует красный цвет (если поддерживается)
   - Добавляет префикс "Ошибка: "

### 8. HumanBeingValidator
Класс для валидации объектов HumanBeing. Проверяет корректность всех полей.

#### Подробное описание методов:
1. `validate(HumanBeing humanBeing)`:
   - Проверяет все поля объекта
   - Возвращает список ошибок
   - Пропускает null значения для nullable полей

2. `validateName(String name)`:
   - Проверяет корректность имени
   - Проверяет на null и пустую строку
   - Проверяет допустимые символы

3. `validateCoordinates(Coordinates coordinates)`:
   - Проверяет корректность координат
   - Проверяет на null
   - Проверяет значения x и y

4. `validateImpactSpeed(Float impactSpeed)`:
   - Проверяет корректность скорости удара
   - Проверяет на null
   - Проверяет минимальное значение

5. `validateMinutesOfWaiting(Long minutesOfWaiting)`:
   - Проверяет корректность минут ожидания
   - Проверяет на null
   - Проверяет положительное значение

6. `validateSoundtrackName(String soundtrackName)`:
   - Проверяет корректность названия саундтрека
   - Проверяет на null и пустую строку
   - Проверяет допустимые символы

7. `validateWeaponType(WeaponType weaponType)`:
   - Проверяет корректность типа оружия
   - Проверяет на null
   - Проверяет допустимые значения enum

8. `validateCar(Car car)`:
   - Проверяет корректность автомобиля
   - Проверяет на null
   - Проверяет поле cool

### 9. Command
Интерфейс для всех команд. Определяет общий контракт для всех команд.

#### Подробное описание методов:
1. `execute(String args, Printer printer)`:
   - Выполняет команду
   - Принимает аргументы
   - Использует printer для вывода
   - Может выбрасывать исключения

2. `getDescription()`:
   - Возвращает описание команды
   - Описание должно быть кратким
   - Должно объяснять назначение команды

3. `getUsage()`:
   - Возвращает пример использования
   - Показывает формат аргументов
   - Должно быть понятно для пользователя

### 10. Реализации команд
Каждая команда реализует интерфейс Command и выполняет конкретное действие.

#### Подробное описание каждой команды:
1. `AddCommand`:
   - Добавляет новый элемент в коллекцию
   - Запрашивает данные у пользователя
   - Валидирует введенные данные
   - Выводит результат операции

2. `ClearCommand`:
   - Очищает коллекцию
   - Запрашивает подтверждение
   - Выводит количество удаленных элементов

3. `ExecuteScriptCommand`:
   - Выполняет команды из файла
   - Проверяет существование файла
   - Обрабатывает ошибки выполнения
   - Выводит результаты выполнения

4. `ExitCommand`:
   - Завершает работу программы
   - Сохраняет коллекцию в файл
   - Выводит сообщение о завершении

5. `FilterBySoundtrackNameCommand`:
   - Фильтрует элементы по названию саундтрека
   - Принимает аргумент - название
   - Выводит найденные элементы
   - Выводит количество найденных элементов

6. `HelpCommand`:
   - Выводит справку по всем командам
   - Форматирует вывод для удобства чтения
   - Сортирует команды по имени

7. `InfoCommand`:
   - Выводит информацию о коллекции
   - Показывает тип коллекции
   - Показывает количество элементов
   - Показывает дату инициализации

8. `PrintFieldDescendingWeaponTypeCommand`:
   - Выводит типы оружия по убыванию
   - Игнорирует null значения
   - Форматирует вывод для удобства чтения

9. `RemoveAllByMinutesOfWaitingCommand`:
   - Удаляет элементы по минутам ожидания
   - Принимает аргумент - минуты
   - Выводит количество удаленных элементов
   - Запрашивает подтверждение

10. `RemoveByIdCommand`:
    - Удаляет элемент по ID
    - Принимает аргумент - ID
    - Выводит результат операции
    - Обрабатывает несуществующие ID

11. `RemoveGreaterCommand`:
    - Удаляет элементы, большие заданного
    - Запрашивает данные для сравнения
    - Выводит количество удаленных элементов
    - Запрашивает подтверждение

12. `RemoveLowerCommand`:
    - Удаляет элементы, меньшие заданного
    - Запрашивает данные для сравнения
    - Выводит количество удаленных элементов
    - Запрашивает подтверждение

13. `SaveCommand`:
    - Сохраняет коллекцию в файл
    - Проверяет возможность записи
    - Выводит результат операции
    - Обрабатывает ошибки записи

14. `ShowCommand`:
    - Выводит все элементы коллекции
    - Сортирует элементы по имени
    - Форматирует вывод для удобства чтения
    - Выводит сообщение для пустой коллекции

15. `UpdateCommand`:
    - Обновляет элемент по ID
    - Принимает аргумент - ID
    - Запрашивает новые данные
    - Выводит результат операции

## Работа системы

1. При запуске программы:
   - Создается экземпляр UserManager
   - UserManager создает CommandManager
   - CommandManager регистрирует все команды
   - UserManager загружает коллекцию через XMLSerializer
   - XMLSerializer использует XMLReader для чтения файла
   - UserManager запускает основной цикл

2. При выполнении команды:
   - UserManager читает ввод через readLine()
   - UserManager передает команду в CommandManager
   - CommandManager находит нужную команду
   - Команда выполняет действие с коллекцией
   - Результат выводится через Printer

3. При сохранении коллекции:
   - Команда save вызывает XMLSerializer
   - XMLSerializer использует XMLWriter
   - XMLWriter сохраняет данные в файл
   - Результат выводится через Printer

4. При обработке ошибок:
   - Ошибка перехватывается в соответствующем классе
   - Создается понятное сообщение об ошибке
   - Сообщение выводится через Printer
   - Программа продолжает работу

## Обработка ошибок

Система обрабатывает следующие типы ошибок:
- Некорректный ввод
- Ошибки валидации
- Ошибки работы с файлами
- Ошибки выполнения команд

Все ошибки выводятся пользователю в понятном формате.

## Аннотации XML

### @XmlElement
Аннотация `@XmlElement` используется для маркировки полей класса, которые должны быть сериализованы в XML. Эта аннотация является частью JAXB (Java Architecture for XML Binding) и используется для преобразования Java-объектов в XML и обратно.

#### Основные параметры:
- `name` - задает имя XML-элемента (по умолчанию используется имя поля)
- `required` - указывает, является ли элемент обязательным
- `nillable` - разрешает ли null-значения
- `type` - задает тип данных для сериализации

#### Пример использования:
```java
@XmlElement(name = "human_being")
private String name;

@XmlElement(name = "coordinates")
private Coordinates coordinates;

@XmlElement(name = "creation_date")
private Date creationDate;
```

### @XmlRootElement
Аннотация `@XmlRootElement` указывает, что класс является корневым элементом XML-документа.

#### Пример:
```java
@XmlRootElement(name = "human_beings")
public class HumanBeingCollection {
    // ...
}
```

### @XmlAccessorType
Аннотация `@XmlAccessorType` определяет, как JAXB будет получать доступ к полям класса для сериализации.

#### Основные значения:
- `XmlAccessType.FIELD` - доступ к полям напрямую
- `XmlAccessType.PROPERTY` - доступ через геттеры и сеттеры
- `XmlAccessType.PUBLIC_MEMBER` - доступ к публичным полям и свойствам
- `XmlAccessType.NONE` - ручное управление сериализацией

#### Пример:
```java
@XmlAccessorType(XmlAccessType.FIELD)
public class HumanBeing {
    // ...
}
```

### @XmlType
Аннотация `@XmlType` определяет порядок сериализации полей и другие параметры типа.

#### Пример:
```java
@XmlType(propOrder = {"name", "coordinates", "creationDate"})
public class HumanBeing {
    // ...
}
```

### Процесс сериализации/десериализации

1. **Сериализация (Java -> XML)**:
   - JAXBContext создает контекст для сериализации
   - Marshaller преобразует объект в XML
   - Поля, помеченные @XmlElement, включаются в XML
   - Порядок полей определяется @XmlType

2. **Десериализация (XML -> Java)**:
   - JAXBContext создает контекст для десериализации
   - Unmarshaller преобразует XML в объект
   - XML-элементы сопоставляются с полями класса
   - Проверяются ограничения (required, nillable)

#### Пример полного класса с аннотациями:
```java
@XmlRootElement(name = "human_being")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "id",
    "name",
    "coordinates",
    "creationDate",
    "realHero",
    "hasToothpick",
    "impactSpeed",
    "soundtrackName",
    "minutesOfWaiting",
    "weaponType",
    "car"
})
public class HumanBeing {
    @XmlElement(name = "id", required = true)
    private Long id;

    @XmlElement(name = "name", required = true)
    private String name;

    @XmlElement(name = "coordinates", required = true)
    private Coordinates coordinates;

    // ... остальные поля
}
```

### Обработка вложенных объектов

Для вложенных объектов (например, Coordinates или Car) также используются аннотации XML:

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates {
    @XmlElement(name = "x", required = true)
    private Double x;

    @XmlElement(name = "y", required = true)
    private Float y;
}
```

### Обработка коллекций

Для коллекций используется аннотация `@XmlElementWrapper`:

```java
@XmlElementWrapper(name = "human_beings")
@XmlElement(name = "human_being")
private Set<HumanBeing> humanBeings;
```

Это создает следующую структуру XML:
```xml
<human_beings>
    <human_being>
        <!-- поля HumanBeing -->
    </human_being>
    <human_being>
        <!-- поля HumanBeing -->
    </human_being>
</human_beings>
```

## Взаимодействие классов

1. При запуске программы:
   - Создается экземпляр UserManager
   - UserManager создает CommandManager
   - CommandManager регистрирует все команды
   - UserManager загружает коллекцию через XMLSerializer
   - XMLSerializer использует XMLReader для чтения файла
   - UserManager запускает основной цикл

2. При выполнении команды:
   - UserManager читает ввод через readLine()
   - UserManager передает команду в CommandManager
   - CommandManager находит нужную команду
   - Команда выполняет действие с коллекцией
   - Результат выводится через Printer

3. При сохранении коллекции:
   - Команда save вызывает XMLSerializer
   - XMLSerializer использует XMLWriter
   - XMLWriter сохраняет данные в файл
   - Результат выводится через Printer

4. При обработке ошибок:
   - Ошибка перехватывается в соответствующем классе
   - Создается понятное сообщение об ошибке
   - Сообщение выводится через Printer
   - Программа продолжает работу

## Примеры использования

### Пример 1: Добавление элемента
```java
// Пользователь вводит команду
String input = "add";

// UserManager передает команду в CommandManager
commandManager.executeCommand("add", "", printer);

// AddCommand запрашивает данные
printer.println("Введите имя:");
String name = readLine();
// ... запрос остальных полей ...

// Создается новый объект
HumanBeing humanBeing = new HumanBeing(name, ...);

// Объект добавляется в коллекцию
collection.addToCollection(humanBeing);

// Выводится результат
printer.println("Элемент успешно добавлен");
```

### Пример 2: Сохранение коллекции
```java
// Пользователь вводит команду
String input = "save";

// UserManager передает команду в CommandManager
commandManager.executeCommand("save", "", printer);

// SaveCommand вызывает XMLSerializer
xmlSerializer.saveToFile(collection, file);

// XMLSerializer использует XMLWriter
xmlWriter.writeToFile(collection, file);

// Выводится результат
printer.println("Коллекция успешно сохранена");
```

### Пример 3: Фильтрация по саундтреку
```java
// Пользователь вводит команду
String input = "filter_by_soundtrack_name Thriller";

// UserManager передает команду в CommandManager
commandManager.executeCommand("filter_by_soundtrack_name", "Thriller", printer);

// FilterBySoundtrackNameCommand вызывает метод коллекции
List<HumanBeing> filtered = collection.filterBySoundtrackName("Thriller");

// Выводится результат
printer.println("Найдено элементов: " + filtered.size());
for (HumanBeing h : filtered) {
    printer.println(h.toString());
}
```

## Сборка проекта в IntelliJ IDEA

### 1. Настройка Maven
1. Откройте проект в IDEA
2. Убедитесь, что Maven правильно настроен:
   - File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven
   - Проверьте путь к Maven (должен быть установлен)
   - Проверьте настройки Maven home directory
   - Проверьте путь к settings.xml

### 2. Сборка проекта
Есть несколько способов собрать проект:

#### Способ 1: Через Maven панель
1. Откройте Maven панель:
   - Способ 1: В верхнем меню выберите View -> Tool Windows -> Maven
   - Способ 2: Нажмите комбинацию клавиш Alt+1 (Windows/Linux) или Command+1 (macOS)
   - Способ 3: В правой части окна IDEA найдите вкладку "Maven" и кликните по ней

2. Структура Maven панели:
   - В верхней части панели находится поисковая строка для быстрого поиска целей
   - Под ней расположено дерево проекта с разделами:
     * `Lifecycle` - основные фазы сборки
     * `Plugins` - доступные плагины Maven
     * `Dependencies` - зависимости проекта
     * `Profiles` - профили сборки

3. Основные фазы сборки в разделе Lifecycle:
   - `clean` - очистка проекта (удаляет папку target)
   - `validate` - проверка корректности проекта
   - `compile` - компиляция исходного кода
   - `test` - запуск тестов
   - `package` - сборка JAR/WAR файла
   - `verify` - проверка результатов сборки
   - `install` - установка артефакта в локальный репозиторий
   - `deploy` - публикация артефакта в удаленный репозиторий

4. Как выполнить фазу сборки:
   - Двойной клик по нужной фазе в дереве
   - Правый клик -> Run Maven Build
   - Использование кнопки "Execute Maven Goal" (значок молнии) в верхней части панели

5. Дополнительные возможности Maven панели:
   - Просмотр логов сборки в нижней части панели
   - Быстрый доступ к настройкам Maven через контекстное меню
   - Возможность отмены текущей сборки
   - Просмотр зависимостей и их версий
   - Управление профилями сборки

### 3. Запуск собранного JAR
1. После успешной сборки JAR файл будет находиться в:
   - `target/your-project-name.jar`
2. Запустите JAR через терминал:
```bash
java -jar target/your-project-name.jar
```

### 4. Решение частых проблем

#### Проблема 1: Maven не найден
Решение:
1. Установите Maven:
```bash
brew install maven  # для macOS
```
2. Проверьте установку:
```bash
mvn -version
```

#### Проблема 2: Ошибки компиляции
Решение:
1. Выполните clean:
```bash
mvn clean
```
2. Обновите зависимости:
```bash
mvn dependency:purge-local-repository
mvn dependency:resolve
```

#### Проблема 3: Ошибки при сборке JAR
Решение:
1. Проверьте pom.xml:
   - Убедитесь, что mainClass указан правильно
   - Проверьте зависимости
2. Выполните сборку с подробным выводом:
```bash
mvn clean package -X
```

### 5. Полезные команды Maven

1. Очистка и сборка:
```bash
mvn clean package
```

2. Пропуск тестов:
```bash
mvn clean package -DskipTests
```

3. Запуск конкретной фазы:
```bash
mvn compile
mvn test
mvn package
mvn install
```

4. Обновление зависимостей:
```bash
mvn dependency:purge-local-repository
mvn dependency:resolve
```

5. Сборка с подробным выводом:
```bash
mvn clean package -X
```

### 6. Структура проекта после сборки

```
your-project/
├── src/
│   ├── main/
│   │   ├── java/     # Исходный код
│   │   └── resources/ # Ресурсы
│   └── test/         # Тесты
├── target/
│   ├── classes/      # Скомпилированные классы
│   ├── generated-sources/
│   ├── maven-status/
│   └── your-project-name.jar # Собранный JAR
├── pom.xml           # Файл конфигурации Maven
└── README.md         # Документация
```

### 7. Настройка pom.xml

Основные секции pom.xml:
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.your.company</groupId>
    <artifactId>your-project</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>
    
    <dependencies>
        <!-- Зависимости проекта -->
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.your.company.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 8. Советы по работе с Maven в IDEA

1. Используйте Maven панель для быстрого доступа к целям
2. Включите автоимпорт зависимостей:
   - File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven
   - Отметьте "Import Maven projects automatically"
3. Используйте Maven плагины для:
   - Форматирования кода
   - Проверки стиля
   - Генерации документации
4. Регулярно обновляйте зависимости:
   - Maven панель -> Dependencies -> Reload
5. Используйте Maven профили для разных окружений:
   - Разработка
   - Тестирование
   - Продакшн 