# EMTasksRepository
1) Дополнительное задание для 1-1 (CSVGenerator)

    Досточно использовать команду `mvn clean install` , чтобы создать скомпилированный jar-артефакт и скопировать его в локальный репозиторий Maven, что позволяет использовать его как зависимость в других проектах на той же машине.
    После, если потребуется внедрить CSVGenerator в другие Maven проекты, то стоит подключать его как обычные зависимости в pom.xml:
```
<dependency>
    <groupId>org.example</groupId>
    <artifactId>CSVGenerator</artifactId>
    <version>1.0</version>
</dependency>
```

2) Задание для 4-2 (Gradle lib)

    Для того, чтобы загрузить данную зависимость в локальный репозиторий Maven, необходимо сначала скомпилировать проект при помощи `./gradlew build`, а затем использовать команду `./gradlew publishToMavenLocal`.
   После, если потребуется внедрить CSVGenerator в другие Gradle проекты, то стоит подключать его как обычные зависимости в gradle.build:
```
repositories {
    mavenLocal()
}

dependencies {
    implementation 'org.example:CSVGenerator:1.0'
}
```