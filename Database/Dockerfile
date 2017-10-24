FROM postgres:9.6

COPY pgpass.conf /docker-entrypoint-initdb.d/
COPY ./schema/* /docker-entrypoint-initdb.d/
COPY ./sample/* /docker-entrypoint-initdb.d/

