FROM openjdk:8
VOLUME /tmp
ADD target/zone-simulator.jar zone-simulator.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","zone-simulator.jar"]