# Указываем базовый образ, содержащий Java runtime
FROM openjdk:17-jdk

# Создаём переменную, указывающую на местоположение JAR-файла приложения
ARG JAR_FILE=target/*.jar

# Копируем JAR-файл приложения в контейнер
COPY ${JAR_FILE} gateway-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]

# Сообщаем Docker, что контейнер прослушивает трафик на указанном порту
EXPOSE 8080