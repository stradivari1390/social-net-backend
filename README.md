# social-net-backend

## [common](./common/README.md)

## [communications-service](./communications-service/README.md)

## [db-management](./db-management/README.md)

## [gateway-service](./gateway-service/README.md)

## [user-service](./user-service/README.md)

## [.localdev](./.localdev/README.md)

## Инструкция по созданию релизов (manual deploy)
1 Создать от dev ветку с форматом имени release_1.0_yyyy-mm-dd (где 0 - номер спринта)

2 Создать MR в ветку master, слить после апрува

3 Убедиться, что образы сервисов обновились в docker hub

4 Cкопировать docker-compose в специальную папку на удаленном сервере

5 Скопировать папку миграций в специальную папку на удаленном сервере

6 Выполнить docker-compose на сервере

7 Проверить доступность фронта и работоспособность сервисов

## Инструкция по созданию hotfix (частичное обновление сервера)
1 Выполнить билд проекта

2 Выполнить docker build нужного сервиса

3 Выполнить docker push этого сервиса

4 (если нужно) скопировать на сервер docker-compose файл и папку с миграциями

5 Выполнить docker-compose на сервере

6 Проверить доступность фронта и работоспособность сервисов