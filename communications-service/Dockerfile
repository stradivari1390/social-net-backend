# Указываем базовый образ, содержащий Java runtime
FROM openjdk:17-jdk

# Создаём переменную, указывающую на местоположение JAR-файла приложения
ARG JAR_FILE=target/*.jar

# Копируем JAR-файл приложения в контейнер
COPY ${JAR_FILE} communications-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:postgresql://postgres:5432/${postgres_db}","-Dspring.datasource.username=${postgres_user}","-Dspring.datasource.password=${postgres_password}","-jar","communications-service.jar"]

# Сообщаем Docker, что контейнер прослушивает трафик на указанном порту
EXPOSE 8082