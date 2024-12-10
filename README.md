# Сервис Обмен валюты
## О задаче 

Задача заключается в доработке приложения, выполняющего конвертацию рублей в некоторые валюты. В объем доработки входят:
- автоматическое обновление списка доступных для конвертации валют и их текущих курсов из классификатора ЦБ РФ;
- корректировка структуры БД валют;
- дополнение API контроллером для вывода полного списка доступных валют;

Взаимодействие с приложением производится через REST API:

Создание новой записи о валюте

```bash
curl --request POST \
  --url http://localhost:8080/api/currency/create \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Доллар Готэм-Сити",
  "nominal": 3,
  "value": 32.2,
  "isoNumCode": 1337
}'
```

Получение Валюты по id

```bash
curl --request GET \
  --url http://localhost:8080/api/currency/1333
```

Конвертация валюты по числовому коду

```bash
curl --request GET \
--url http://localhost:8080/api/currency/convert?value=100&numCode=840
```
## Используемые технологии

- Spring Boot 2.7
- Maven 3
- Lombok
- Mapstruct
- Liquibase
- PostgreSQL
- JAXB

