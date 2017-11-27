#!/bin/sh

docker build -t quinisela_db .
docker run -d --name quinisela_db quinisela_db -e POSTGRES_PASSWORD=yusuke -d 
docker logs -f -t quinisela_db
