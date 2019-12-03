CREATE USER sparkdash WITH PASSWORD 'sparkdash';
CREATE DATABASE sparkdash WITH OWNER = sparkdash ENCODING = 'UTF8' TABLESPACE = pg_default;

CREATE table step
(
    id     int primary key not null,
    points int             not null
);

insert into step(id, points)
values (1, 0);
insert into step(id, points)
values (2, 30);
insert into step(id, points)
values (3, 50);
insert into step(id, points)
values (4, 50);
insert into step(id, points)
values (4, 70);
insert into step(id, points)
values (5, 100);
insert into step(id, points)
values (6, 150);
insert into step(id, points)
values (7, 200);
insert into step(id, points)
values (8, 250);CREATE table step
(
    id     int primary key not null,
    points int             not null
);

insert into step(id, points)
values (1, 0);
insert into step(id, points)
values (2, 30);
insert into step(id, points)
values (3, 50);
insert into step(id, points)
values (4, 50);
insert into step(id, points)
values (4, 70);
insert into step(id, points)
values (5, 100);
insert into step(id, points)
values (6, 150);
insert into step(id, points)
values (7, 200);
insert into step(id, points)
values (8, 250);
insert into step(id, points)
values (9, 350);
insert into step(id, points)
values (10, 500);


CREATE table team
(
    id              bigserial primary key,
    name            text,
    current_step_id int default 1
);

GRANT ALL PRIVILEGES ON TABLE team TO sparkdash;
GRANT ALL PRIVILEGES ON TABLE team_id_seq TO sparkdash;
GRANT ALL PRIVILEGES ON TABLE step TO sparkdash;

insert into step(id, points)
values (9, 350);
insert into step(id, points)
values (10, 500);


CREATE table team
(
    id              bigserial primary key,
    name            text,
    current_step_id int default 1
);

GRANT ALL PRIVILEGES ON TABLE team TO sparkdash;
GRANT ALL PRIVILEGES ON TABLE team_id_seq TO sparkdash;
GRANT ALL PRIVILEGES ON TABLE step TO sparkdash;
