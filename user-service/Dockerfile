# Указываем базовый образ, содержащий Java runtime
FROM openjdk:17-jdk-slim

# Создаём переменную, указывающую на местоположение JAR-файла приложения
ARG JAR_FILE=target/*.jar

# Заполняем переменные окружения значениями по умолчанию
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV DB_NAME=socialnet

# Копируем JAR-файл приложения в контейнер
COPY ${JAR_FILE} user-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:postgresql://postgres:5432/${DB_NAME}","-Dspring.datasource.username=${DB_USERNAME}","-Dspring.datasource.password=${DB_PASSWORD}","-jar", "user-service.jar"]

# Сообщаем Docker, что контейнер прослушивает трафик на указанном порту
EXPOSE 8081