CREATE USER sparkdash WITH PASSWORD 'sparkdash';
CREATE DATABASE sparkdash WITH OWNER = sparkdash ENCODING = 'UTF8' TABLESPACE = pg_default;

CREATE table team (
    id bigserial primary key,
    name text,
    stepcount integer default 0
);
