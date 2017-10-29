--drop table master_table;
--drop table detail;

create database AdelaideDB;
\c adelaidedb
create table master(
id integer default 0 primary key,
code text unique,
name text
);

create table detail(
    id integer default 0 primary key,
    code text unique,
    name text,
    master_code text references master(code)
);

\c postgres
