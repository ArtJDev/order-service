# order-service (ITBooks)
Сервис для оформления заказа из интернет магазина ITBooks

**Стек: Java 17, Spring Boot 3.0, Spring WebFlux, Spring Data JDBC, Spring Data R2DBC, PostgreSQL, Flyway, JUnit, Mockito, Testcontainers, Gradle, Docker.**
## Описание
Проект представляет собой микросервис для оформления заказов из интернет магазина ITBooks. 
Проект написан в рективном стиле с использованием WebClient и реактивным драйвером R2DBC базы данных PostgreSQL.
Сервис взаимодействует с микросервисом [product-service](https://github.com/ArtJDev/product-service) получая от него данные о книгах для оформления заказа.
Написаны модульные тесты с использованием JUnit, Mockito, библиотек Jakarta Validation, JacksonTester, интеграционные тесты с использованием Testcontainers и WebTestClient.
## Запуск
Для полноценной работы сервиса необходим работающий сервис [product-service](https://github.com/ArtJDev/product-service) 
и запущенный контенер с базой данных (см. раздел ["Запуск"](https://github.com/ArtJDev/product-service/blob/main/README.md#запуск))

Запуск приложения осуществляется командой `./gradlew bootRun`.
## Спецификация REST API
| Endpoint | HTTP method | Request body | Status | Response body | Описание |
|------------------|--------|--------------|-----|----------|-----------------------------------------------------------------|
| /orders          |  GET   |	             | 200 | Order[ ]	|	Получить все заказы |
| /orders          |  POST  | OrderRequest | 200 | Order    |	Оформить новый заказ с выбранной книгой и выбранным количеством |

## Тестовые данные
В базе данных сервиса [product-service](https://github.com/ArtJDev/product-service) существуют книги с артиклями 00100, 00101, 00102.

Json объект для POST запросов {
    "article": "00100",
    "quantity": 3
}

Заказы с артиклями существующих книг и разрешенного их количества будут иметь статус "Accepted" (принятые), иначе "Rejected" (отклоненные).
