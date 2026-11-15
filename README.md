# User Service

Консольное приложение на Java для управления пользователями (CRUD: создание, чтение, обновление, удаление).
Работает с PostgreSQL через Hibernate 7, без Spring. Интерфейс — интерактивное консольное меню.

## Требования

- **JDK 27** (проект собирается с `maven.compiler.source/target = 27`)
- **Maven 3.9+**
- **Docker** и **Docker Compose** (для базы данных)

## Запуск базы данных

PostgreSQL поднимается в Docker-контейнере из `docker-compose.yml`:

```bash
docker compose up -d
```

После запуска доступны:

| Параметр     | Значение       |
|--------------|----------------|
| Хост         | `localhost`    |
| Порт         | `5432`         |
| База данных  | `appdb`        |
| Пользователь | `appuser`      |
| Пароль       | `spasihospadi` |

Проверить, что контейнер поднялся:

```bash
docker compose ps
```

Остановить базу (данные сохраняются в volume `pgdata`):

```bash
docker compose down
```

Полностью удалить данные и пересоздать базу с нуля:

```bash
docker compose down -v
docker compose up -d
```

## Конфигурация приложения

Настройки подключения лежат в `src/main/resources/hibernate.properties`

> **Важно:** `hibernate.hbm2ddl.auto=create` означает, что схема базы пересоздаётся
> при каждом запуске приложения, а все данные стираются. При каждом запуске вы
> получаете чистую базу.

## Сборка и запуск

Сборка:

```bash
mvn clean compile
```

Запуск (Linux/macOS):

```bash
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "target/classes:
