# classificator
Documentation work in progress!

![GitHub](https://img.shields.io/github/license/Cuupa/classificator) ![CI](https://github.com/Cuupa/classificator/workflows/CI/badge.svg) ![GitHub issues](https://img.shields.io/github/issues-raw/Cuupa/classificator) ![GitHub pull request](https://img.shields.io/github/issues-pr-raw/Cuupa/classificator)

## Content
- [About this project](https://github.com/Cuupa/classificator#about-this-project)
- [How to contribute](https://github.com/Cuupa/classificator#how-to-contribute)
- [How it works](https://github.com/Cuupa/classificator#how-it-works)
- [Installation](https://github.com/Cuupa/classificator#installation)
  - [Standalone](https://github.com/Cuupa/classificator#standalone)
  - [Docker](https://github.com/Cuupa/classificator#docker)
  - [Synology Docker](https://github.com/Cuupa/classificator#synology-docker)
  - [Changing the configuration](https://github.com/Cuupa/classificator#changing-the-configuration)
  - [Install a new Knowledgebase](https://github.com/Cuupa/classificator#install-a-new-knowledgebase)

## About this project

This project is a simple classification engine written in Kotlin and using Spring Boot as a framework.

This project is provided via the MIT-licence, which is free of charge. But if you want to support me, you can spend me a
beer or a coffee. If you want to participate, feel free to create pull requests, fork this project or hit me up with
suggestions or code reviews.

THIS IS A WORK IN PROGRESS and done in my spare time.

## How to contribute
If you want to participate, feel free to create pull requests, fork this project, create new issues or hit me up with suggestions.
When creating an issue or a pull request, please be as detailed as possible.

"I want to participate, but I know nothing about programming 😔"
- No problem. You can contribute by
  providing [topic definitions](https://github.com/Cuupa/classificator/tree/master/knowledgebase) or contribute by
  providing feedback, make some suggestions eg. If you want to contribute to your topic, open a new issue providing your
  suggested changes but also supply any text you have tested with. If you want to create or fine tune a topic, create a
  pull request and I'll give it a shot.

If you think this project is awesome, you can spend me a beer or a coffee.

![BuyMeACoffee](https://img.shields.io/badge/Support%20%20me-Buy%20me%20a%20coffee-success?logo=buymeacoffee&link=https://buymeacoff.ee/Cuupa)

[Direct link](https://buymeacoff.ee/Cuupa)

## How it works

Currently it is just a keyword classification engine with some tweaks. It uses the levensthein algorithm to counter
spelling or OCR errors.

It tries to match the topics, sender and metadata, provided in the 7zip archive in "knowledgebase/kb-{version
number}.db". If no sender matches, it tries to determine the sender via REGEX, removing the ones with more then six
words, counting the occurences in the text and taking the occurences times the length of the String.

There is a simple test GUI at http://addressofyour.server/

Currently I'm working on lemmatizing and different languages.

## Installation

### Standalone

Since it is a simple .jar file, which can run on any device
featuring [Java](https://www.java.com/de/download/manual.jsp), you can run it by simply doubleclicking. Be aware of
the [application configuration](https://github.com/Cuupa/classificator/tree/master/src/main/resources/application.yml).

The default port as in said configuration is "8080"

``` yaml
server.port: 8080
```

So if you want to access the webUI on your local machine navigate to "https://localhost:8080"

There is also the configuration entry for the knowledgebase:

``` yaml
classificator.kbfiles: knowledgebase
```

This entry means, that there has to be a folder named "knowlegebase" beside your jar. You can specify which
knowledgebase to use, by changing it (How that works will be covered by me
a [few lines down below](https://github.com/Cuupa/classificator#Changing-the-configuration)). If this entry only
contains a folder name without the specific knowlegedbase, it will load the file with the highest version tag in that
folder

### Docker

If you can use a dockerfile, you can create one like this:
Note to self: Need to test this!

``` dockerfile
FROM openjdk:15-jdk
MAINTAINER Cuupa
WORKDIR /opt/app/classificator
COPY knowlegebase/kb-1.0.0.db ./knowledgebase/kb-1.0.0.db
COPY *.jar ./app.jar

EXPOSE 8080:8080

ENTRYPOINT ["java", "-jar", "./app.jar", "-Dserver.port=8080", "-Dserver_port=8080", "-Dclassificator.kbfiles=./knowlegebase/kb-1.0.0.db"]
```

Please change the port of the application and change it accordingly.

### Synology Docker

1. Create yourself a folder, where you upload the files to

   ![folder](https://github.com/Cuupa/classificator/blob/master/documentation/docker-classificator.png "folder")

### Changing the configuration

If you know about programming: Great! You can change it as you like for example

``` yaml
classificator.kbfiles: knowledgebase/kb-1.0.0.db
```

or

``` yaml
server.port: 1234
```

If you don't, don't panic. You can run it by typing

``` shell
java -jar app.jar -Dserverport=1234 -Dclassificator.kbfiles:knowlegebase/kb-1.0.0.db
```

Notice the "-D" before each parameter. This is mandatory.

Of course, you can use absolute paths like

``` shell
java -jar app.jar -Dclassificator.kbfiles:"C:\Users\JohnDoe\My Programs\classificator\knowlegebase\kb-1.0.0.db"
```

Notice that, you need to quote the value as soon as you have spaces in a parameter

### Install a new Knowledgebase

Just copy the new database to your corresponding directory and restart the application