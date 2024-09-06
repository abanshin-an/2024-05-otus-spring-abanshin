# 2024-05-otus-spring-abanshin

For OTUS Spring homeworks

## Задание 1
Для сборки проекта использовать команду
```shell
mvn -f hw01a/pom.xml clean compile checkstyle:checkstyle package 
```
Запускать командой
```shell
java  -jar hw01a/target/hw01-1.0.jar
```

## Задание 2
```shell
mvn -f hw02-annotation-config/pom.xml clean compile checkstyle:checkstyle package 
```

```shell
java  -jar hw02-annotation-config/target/hw02-annotation-config-1.0.jar
```

## Задание 3
Для сборки проекта использовать команду
```shell
mvn -f hw03/pom.xml clean compile checkstyle:checkstyle package
```
Английская локализация
```shell
java  -jar -Dtest.locale=en-US hw03/target/hw03-spring-boot-1.0.jar
```
Русская локализация
```shell
java  -jar -Dtest.locale=ru-RU hw03/target/hw03-spring-boot-1.0.jar
```

## Задание 5
```shell
mvn -f hw05/pom.xml clean compile checkstyle:checkstyle package 
```

```shell
java  -jar hw05/target/hw05-jdbc-1.0.jar
```
