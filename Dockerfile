FROM openjdk:21
MAINTAINER protege.stanford.edu

ARG JAR_FILE
COPY target/${JAR_FILE} webprotege-gh-issues-service.jar
ENTRYPOINT ["java","-jar","/webprotege-gh-issues-service.jar"]