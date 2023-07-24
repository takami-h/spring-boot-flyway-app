# Spring Boot + Flyway example app

Spring Boot with Flyway migrations example.

This app runs with h2 database for development, and with PostgreSQL for production.

## How to build & run.

### run with h2 database

Build and run app with h2 database.

```
$ ./mvnw spring-boot:run
```

### run with PostgreSQL

Run PostgreSQL database (docker example below).

```
$ docker run --name tododb \
    -e POSTGRES_DB=tododb \
    -e POSTGRES_USER=tododb -e POSTGRES_PASSWORD=p@ssw0rd \
    -e POSTGRES_INITDB_ARGS="--encoding=UTF-8 --locale=C" \
    -p 5432:5432 \
    -d postgres:13
```

Build and run app as production mode.

```
$ ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```