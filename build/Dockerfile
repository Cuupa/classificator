FROM openjdk:15-jdk
MAINTAINER Cuupa

WORKDIR /opt/app/classificator

ADD https://github.com/Cuupa/classificator/releases/download/1.2.0/app-1.2.0_Release.jar app.jar
ADD https://github.com/Cuupa/classificator/releases/download/1.2.0/kb-1.0.3.db knowledgebase/kb-1.0.3.db
ADD https://github.com/Cuupa/classificator/releases/download/1.2.0/configuration.yml /.configuration.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080", "--knowlegde_base=/opt/app/classificator/knowlegebase/kb-1.0.3.db"]
