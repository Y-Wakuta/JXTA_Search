#!/bin/sh

docker stop `docker ps -a -q`
docker rm `docker ps -a -q`

docker build -t jxta_search .
docker run -d --name jxtadb jxta_search 
docker logs -f -t jxtadb