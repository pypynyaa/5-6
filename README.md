# Лабораторная работа №5: Управление коллекцией объектов

## Описание проекта
Проект представляет собой консольное приложение для управления коллекцией объектов типа `HumanBeing`. Коллекция хранится в XML-файле и поддерживает различные операции с элементами.

## Функциональность
- Загрузка коллекции из XML-файла
- Сохранение коллекции в XML-файл
- Добавление новых элементов
- Обновление существующих элементов
- Удаление элементов по различным критериям
- Фильтрация и сортировка элементов
- Выполнение скриптов с командами
- История выполненных команд

## Требования
- Java 11 или выше
- Maven 3.6 или выше

## Установка и запуск
1. Клонируйте репозиторий:
```bash
git clone https://github.com/pypynyaa/lab_five_itmo.git
```

2. Перейдите в директорию проекта:
```bash
cd lab_five_itmo
```

3. Соберите проект с помощью Maven:
```bash
mvn clean package
```

4. Запустите приложение:
```bash
java -jar target/lab5-1.0-SNAPSHOT.jar
```

## Доступные команды
- `help` - вывести справку по доступным командам
- `info` - вывести информацию о коллекции
- `show` - вывести все элементы коллекции
- `add` - добавить новый элемент
- `update id` - обновить элемент по id
- `remove_by_id id` - удалить элемент по id
- `clear` - очистить коллекцию
- `execute_script file_name` - выполнить скрипт из файла
- `exit` - завершить программу
- `remove_greater` - удалить элементы, большие заданного
- `remove_lower` - удалить элементы, меньшие заданного
- `history` - показать историю команд
- `remove_all_by_soundtrack_name name` - удалить элементы по названию саундтрека
- `filter_greater_than_car` - вывести элементы с машиной лучше заданной
- `print_descending` - вывести элементы в порядке убывания
- `save` - сохранить коллекцию в файл

## Структура проекта
```
src/
├── main/
│   ├── java/
│   │   ├── command/      # Команды приложения
│   │   ├── interfaces/   # Интерфейсы
│   │   ├── manager/      # Менеджеры и валидаторы
│   │   ├── model/        # Модели данных
│   │   ├── parse/        # Парсеры XML
│   │   └── utility/      # Вспомогательные классы
│   └── resources/        # Ресурсы приложения
└── test/                 # Тесты
```

## Модель данных
Класс `HumanBeing` содержит следующие поля:
- `id` - уникальный идентификатор
- `name` - имя (не может быть null)
- `coordinates` - координаты
- `creationDate` - дата создания
- `realHero` - является ли героем
- `hasToothpick` - есть ли зубочистка
- `impactSpeed` - скорость удара
- `soundtrackName` - название саундтрека
- `minutesOfWaiting` - минуты ожидания
- `weaponType` - тип оружия
- `car` - машина

## Формат XML-файла
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<humanBeings>
    <humanBeing>
        <id>1</id>
        <name>John Doe</name>
        <coordinates>
            <x>10</x>
            <y>20</y>
        </coordinates>
        <creationDate>2024-03-20T12:00:00</creationDate>
        <realHero>true</realHero>
        <hasToothpick>false</hasToothpick>
        <impactSpeed>15.5</impactSpeed>
        <soundtrackName>Soundtrack 1</soundtrackName>
        <minutesOfWaiting>30</minutesOfWaiting>
        <weaponType>PISTOL</weaponType>
        <car>
            <name>Car 1</name>
            <cool>true</cool>
        </car>
    </humanBeing>
</humanBeings>
```

## Автор
- [pypynyaa](https://github.com/pypynyaa)

## Лицензия
MIT License 