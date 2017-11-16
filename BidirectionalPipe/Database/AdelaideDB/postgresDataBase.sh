#!/bin/sh

docker build -t adelaide_db .
docker run -d --name adelaide_db adelaide_db -e POSTGRES_PASSWORD=yusuke -d 
docker logs -f -t adelaide_db
