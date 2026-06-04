# Microservice Refactoring Report

## Project structure

```
taxi-system/                    (Maven parent, artifactId: taxi-system)
├── passenger-service/         port 8081, DB: passenger_db
├── driver-service/            port 8082, DB: driver_db
├── trip-service/              port 8083, DB: trip_db
└── rating-service/            port 8084, DB: rating_db
```

## JPA object references (entity associations)

| Связь | Тип | Файл | Рекомендация |
|-------|-----|------|--------------|
| **Car → Driver** | `@ManyToOne` + `Driver driver` | `driver-service/.../car/model/entity/Car.java:27-29` | Остаётся в **driver-service** (один bounded context). При выделении `car` в отдельный сервис — заменить на `long driverId`. |

## Logical references (Long id — already microservice-friendly)

| Сущность | Поля | Файл |
|----------|------|------|
| Trip | `driverId`, `passengerId` | `trip-service/.../trip/model/entity/Trip.java:20-24` |
| Rating | `tripId`, `driverId`, `passengerId` | `rating-service/.../rating/model/entity/Rating.java:17-24` |

## Cross-service compile-time coupling (resolved / pending)

### Trip-service

| Было | Стало | Файлы |
|------|-------|-------|
| `DriverRepository`, `PassengerRepository` | `DriverClient`, `PassengerClient` | `trip-service/.../trip/client/*.java` |
| Прямая валидация в `TripServiceImpl` | `driverClient.ensureActive()`, `passengerClient.ensureActive()` | `trip-service/.../trip/service/impl/TripServiceImpl.java:88-91` |
| Stub (временно) | `StubDriverClient`, `StubPassengerClient` | `trip-service/.../trip/client/impl/*.java` |

**Sprint 2:** реализовать Feign-клиенты вместо stub — проверка существования водителя/пассажира и флага `deleted`.

### Rating-service

| Было | Стало | Файлы |
|------|-------|-------|
| `TripRepository`, `Trip`, `TripStatus` | `TripClient`, `TripForRating` | `rating-service/.../rating/client/*.java` |
| Загрузка поездки и статуса `COMPLETED` | `tripClient.findForRating()` | `rating-service/.../rating/service/impl/RatingServiceImpl.java:49-54` |
| Stub (временно) | `StubTripClient` | `rating-service/.../rating/client/impl/StubTripClient.java` |

**Sprint 2:** Feign к `trip-service` — `exists`, получение `driverId`/`passengerId` и проверка `COMPLETED`.

## Database foreign keys (Liquibase)

| FK (монолит) | Сервис | Статус после split |
|--------------|--------|-------------------|
| `fk_cars_driver` | driver-service | **Сохранён** (внутри сервиса) |
| `fk_trips_driver` | trip-service | **Удалён** — только колонка `driver_id` |
| `fk_trips_passenger` | trip-service | **Удалён** — только колонка `passenger_id` |
| `fk_ratings_trip` | rating-service | **Удалён** — только колонка `trip_id` |

## Car mapper (intra driver-service)

| Место | Деталь |
|-------|--------|
| `CarMapper.java:10` | `driverId` ← `driver.id` (JPA navigation) |
| `CarServiceImpl.java:82-89` | `findActiveDriver()` загружает `Driver` entity |

Не требует межсервисных изменений, пока `car` остаётся в driver-service.

## Temporary behavioral notes (stubs)

- **trip-service:** `StubDriverClient` / `StubPassengerClient` не выполняют удалённую валидацию — поездки можно создать с несуществующими или удалёнными id.
- **rating-service:** `StubTripClient` всегда считает поездку существующей и завершённой; `driverId`/`passengerId` временно `0` до Feign.

## Sprint 2 checklist

1. Добавить OpenFeign (или REST-клиенты) — `DriverClient`, `PassengerClient`, `TripClient`.
2. Удалить stub-реализации или перевести на `@Profile("local")`.
3. Согласовать идентификаторы между БД (seed / синхронизация).
4. Рассмотреть API Gateway и service discovery (вне текущего scope).
