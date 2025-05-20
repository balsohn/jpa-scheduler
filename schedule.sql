DROP DATABASE scheduler;

CREATE DATABASE IF NOT EXISTS scheduler;
USE scheduler;

CREATE TABLE IF NOT EXISTS users(
    id          BIGINT          AUTO_INCREMENT  PRIMARY KEY ,
    username    VARCHAR(50)     NOT NULL ,
    email       VARCHAR(100)    NOT NULL    UNIQUE ,
    password    VARCHAR(100)    NOT NULL ,
    created_at  DATETIME        NOT NULL ,
    modified_at DATETIME        NOT NULL
);

CREATE TABLE IF NOT EXISTS schedules(
    id          BIGINT          AUTO_INCREMENT  PRIMARY KEY ,
    title       VARCHAR(100)    NOT NULL ,
    content     TEXT            NOT NULL ,
    user_id     BIGINT          NOT NULL ,
    created_at  DATETIME        NOT NULL ,
    modified_at DATETIME        NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments(
    id          BIGINT          AUTO_INCREMENT  PRIMARY KEY ,
    content     TEXT            NOT NULL ,
    user_id     BIGINT          NOT NULL ,
    schedule_id BIGINT          NOT NULL ,
    created_at  DATETIME        NOT NULL ,
    modified_at DATETIME        NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES users(id) ,
    FOREIGN KEY (schedule_id) REFERENCES schedules(id)
)