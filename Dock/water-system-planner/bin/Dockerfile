FROM openjdk:8
VOLUME /tmp
ADD target/water-system-planner.jar water-system-planner.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","water-system-planner.jar"]