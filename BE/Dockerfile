FROM maven:latest AS build-project
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17
ADD target/ds-2020-0.0.1-SNAPSHOT.jar spring-docker.jar
ENTRYPOINT ["java", "-jar","spring-docker.jar"]
EXPOSE 8080

#FROM maven:3.6.3-jdk-11 AS builder
#
#COPY ./src/ /root/src
#COPY ./pom.xml /root/
#COPY ./checkstyle.xml /root/
#WORKDIR /root
#RUN mvn package
#RUN java -Djarmode=layertools -jar /root/target/ds-2020-0.0.1-SNAPSHOT.jar list
#RUN java -Djarmode=layertools -jar /root/target/ds-2020-0.0.1-SNAPSHOT.jar extract
#RUN ls -l /root
#
#FROM openjdk:11.0.6-jre
#
#COPY --from=builder /root/dependencies/ ./
#COPY --from=builder /root/snapshot-dependencies/ ./
#
#RUN sleep 10
#COPY --from=builder /root/spring-boot-loader/ ./
#COPY --from=builder /root/application/ ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher","-XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m"]
