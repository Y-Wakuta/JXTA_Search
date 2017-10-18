FROM postgres:9.6

copy pgpass.conf /docker-entrypoint-initdb.d/
copy ./schema/* /docker-entrypoint-initdb.d/
copy ./sample/* /docker-entrypoint-initdb.d/

