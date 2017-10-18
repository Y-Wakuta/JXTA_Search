--drop table master_table;
--drop table detail;

create table master_table(
id integer default 0 primary key,
code text unique,
name text
);

create table detail(
    id integer default 0 primary key,
    code text unique,
    name text,
    master_code text references master_table(code)
);
