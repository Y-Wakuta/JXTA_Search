drop table master_table;
drop table detail;

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

insert into master_table values
(1,'1111','顧客1'),
(2,'2222','顧客2'),
(3,'3333','顧客3'),
(4,'4444','顧客4');

insert into detail values
(1,'11','注文1','1111'),
(2,'22','注文2','1111'),
(3,'33','注文3','2222'),
(4,'44','注文4','2222'),
(5,'55','注文5','2222'),
(6,'66','注文6','3333'),
(7,'77','注文7','3333'),
(8,'88','注文8','4444'),
(9,'99','注文9','4444');