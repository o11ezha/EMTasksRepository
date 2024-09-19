# EMTasksRepository
1) Дополнительное задание для 1-1 (CSVGenerator)

    Досточно использовать команду `mvn clean install` , чтобы создать скомпилированный jar-артефакт и скопировать его в локальный репозиторий Maven, что позволяет использовать его как зависимость в других проектах на той же машине.
    После, если потребуется внедрить CSVGenerator в другие проекты, то стоит подключать его как обычные зависимости в pom.xml:
```
<dependency>
    <groupId>org.example</groupId>
    <artifactId>CSVGenerator</artifactId>
    <version>1.0</version>
</dependency>
```
