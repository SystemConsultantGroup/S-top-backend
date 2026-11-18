# 2026-11-18 CVE-2026-114938 대응: 패치된 Corretto 베이스로 다시 빌드
FROM amazoncorretto:17-alpine

ARG JAR_FILE=build/libs/stop-0.0.1-SNAPSHOT.jar
WORKDIR /home/app
COPY ${JAR_FILE} /home/app/app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/home/app/app.jar"]
