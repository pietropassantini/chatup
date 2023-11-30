-- Questo è il file init.sql
CREATE DATABASE file:chatupdb;
CREATE USER "chatusr" PASSWORD '123' ADMIN

CREATE TABLE GLOBAL_MSG (
    id integer,
    msg varchar,
    time timestamp,
    from varchar
);