CREATE USER sprkdash;
CREATE DATABASE sparkdash with owner sparkdash;

CREATE table team (
    id bigserial primary key,
    name text,
    stepcount integer default 0
);
