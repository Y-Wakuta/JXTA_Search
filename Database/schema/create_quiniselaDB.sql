--drop table student;
--drop table teacher;

create SEQUENCE id_student_seq
    start with 1
    increment by 1
    cache 100;

create table student(
id integer default nextval('id_student_seq'::regclass) primary key,
code text default ''::text UNIQUE,
name text default ''::text
);

create SEQUENCE id_teacher_seq
    start with 1
    increment by 1
    cache 100;

create table teacher(
    id integer default nextval('id_teacher_seq'::regclass) primary key,
    code text default ''::text UNIQUE,
    name text default ''::text
);

create table student_teacher(
    student_code text REFERENCES student(code),
    teacher_code text REFERENCES teacher(code)
);
