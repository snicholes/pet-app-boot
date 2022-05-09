FROM java:8
COPY target/*.jar pet-app.jar
ENTRYPOINT ["java", "-jar", "pet-app.jar"]