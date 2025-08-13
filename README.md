# Учебный проект: Клиент-серверное приложение

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.6-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org)

Проект демонстрирует реализацию клиент-серверного взаимодействия через TCP-сокеты с использованием Spring Framework, сохранением данных в PostgreSQL и логгированием при помощи библиотеки log4j. Проект собран на Maven.

## Ключевые особенности
- **TCP-коммуникация**: Общение между клиентом и сервером происходит при помощи протокола TCP
- **Spring**: Управление зависимостями
- **Слой данных**: 
  - JDBC для работы с PostgreSQL, с возможностью улучшения до Hibernate
- **Многопоточность**: Обработка клиентских подключений в отдельных потоках, при помощи ForKJoinPool
- **Использование паттернов проектирования**: Используются паттерны Singleton, Command.


##  Технологический стек
| Компонент       | Технологии                             |
|-----------------|----------------------------------------|
| **Backend**     | Spring |
| **База данных** | PostgreSQL 15+ |
| **Сеть**        | TCP сокеты, NIO  |
| **Сборка**      | Maven |

##  Запуск проекта

### Предварительные требования
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### Настройка базы данных
1. Создайте БД в PostgreSQL:
```sql
CREATE DATABASE laba7
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE IF NOT EXISTS public.collection
(
    key bigint NOT NULL,
    id_coll bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    "x_сoordinate" bigint NOT NULL,
    y_coordinate bigint NOT NULL,
    date text COLLATE pg_catalog."default" NOT NULL,
    engine_power bigint NOT NULL,
    capacity bigint NOT NULL,
    vehicle_type text COLLATE pg_catalog."default" NOT NULL,
    fuel_type text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT collection_pkey PRIMARY KEY (id_coll)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.collection
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.proxy
(
    user_id bigint NOT NULL,
    coll_id bigint NOT NULL,
    CONSTRAINT coll_con FOREIGN KEY (coll_id)
        REFERENCES public.collection (id_coll) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT user_con FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.proxy
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    login text COLLATE pg_catalog."default" NOT NULL,
    password text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Users_pkey" PRIMARY KEY (user_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;
```
2. Подключение настраивается в config.properties
