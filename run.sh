#!/bin/bash

# Компиляция проекта
mvn clean compile

# Сборка classpath
CLASSPATH="target/classes"
for jar in $(find ~/.m2/repository -name "*.jar"); do
    CLASSPATH="$CLASSPATH:$jar"
done

# Запуск программы с data.xml
java -cp "$CLASSPATH" Main data.xml

 