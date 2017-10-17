#!/bin/sh

docker rm `docker ps -a -q`

#Adelaide Container
#Dockerコンテナを立てる
docker run -d --name postgres_Adelaide -e POSTGRES_PASSWORD=1 -p 15432:5432 postgres:9.6
psql -f 

docker run -d --name postgres_Quinisela -e POSTGRES_PASSWORD=2 -p 15433:5433 postgres:9.6

