#!/bin/sh

docker stop `docker ps -a -q`
docker rm `docker ps -a -q`
docker rmi $(docker images -q)

docker build -t jxta_search_1 .
docker run -p 5432:5432 --name jxtadb_1 jxta_search_1 
docker logs -f -t jxtadb_1
