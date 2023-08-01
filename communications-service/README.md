## communication-service

Сервис **communicate-service** отвечает за обеспечение коммуникации и взаимодействия (обмен сообщениями, новости, оповещения и поиск).

Для того чтобы запустить сервис необходимо:
1. Подключить базу данных (см. [localdev/README.md](../.localdev/README.md) или [db-management/README.md](../db-management/README.md))
2. Настроить конфигурацию:
- Откройте меню "Run" в верхней панели навигации IntelliJ IDEA.

- Выберите пункт "Edit Configurations".

- В окне "Run/Debug Configurations" найдите конфигурацию запуска **communication-service**.

- В разделе "VM Options" нажмите на кнопку "Modify Options".

- Выберите пункт "Add VM Options".

- В поле ввода введите необходимые переменные в формате:
```
-Dspring.datasource.url=jdbc:postgresql://localhost:5433/socialnet  -Dspring.datasource.username=postgres  -Dspring.datasource.password=postgres
```
3. Запустить сервис из верхней панели.