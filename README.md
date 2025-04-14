# List Book

Система управления книгами на Spring Boot с использованием Java 21 и PostgreSQL.

## Запуск приложения

Для запуска приложения в контейнере:

```bash
# Первый запуск
docker-compose up -d

# Пересборка с учетом изменений
docker-compose up --build -d

# Полная пересборка
docker-compose down && docker-compose up --build -d
```

## Доступ к приложению

### REST API
- Документация REST API (Swagger): http://localhost:8080/swagger-ui/index.html
- REST эндпоинты доступны по пути: http://localhost:8080/api/books

### MVC Web-интерфейс
- Web-интерфейс: http://localhost:8080/books

## Пользователи системы

В системе доступны следующие пользователи:

| Логин | Пароль | Роль |
|-------|--------|------|
| admin | admin  | ADMIN|
| user  | user   | USER |
