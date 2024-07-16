FROM adoptopenjdk/openjdk21:alpine-jre

WORKDIR /app

COPY target/online-cinema.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
