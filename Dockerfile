FROM gradle:jdk17-alpine AS gradle
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM openjdk:17 AS runtime
WORKDIR /app
ENV PORT 8080
ENV SPRING_PROFILE development
#production
ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=gradle ${JAR_FILE} /app/app.jar

RUN chown -R 1000:1000 /app
USER 1000:1000

ENTRYPOINT ["java","-jar","-Dserver.port=${PORT}","-Dspring.profiles.active=${SPRING_PROFILE}","app.jar"]