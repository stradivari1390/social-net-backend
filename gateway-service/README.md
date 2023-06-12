## gateway-service

Gateway Service (сервис шлюза) является промежуточным компонентом в архитектуре микросервисов, который отвечает за обработку внешних запросов и направление их к соответствующим микросервисам. Он играет роль точки входа для клиентских приложений или других сервисов, обеспечивая им единый и унифицированный интерфейс.

### Конфигурация сервиса


| property                       | var              | text                                        |
|--------------------------------|------------------|---------------------------------------------|
| spring.cloud.gateway.routes[0] | id               | user-service                                |
| spring.cloud.gateway.routes[0] | uri              | lb://user-service                           |
| spring.cloud.gateway.routes[0] | predicates[0]    | Path=/user-service/** - предикат маршрута для запросов, начинающихся с "/user-service/" |
| spring.cloud.gateway.routes[1] | id               | communications-service                      |
| spring.cloud.gateway.routes[1] | uri              | lb://communications-service                 |
| spring.cloud.gateway.routes[1] | predicates[0]    | Path=/communications-service/** - предикат маршрута для запросов, начинающихся с "/communications-service/" |

