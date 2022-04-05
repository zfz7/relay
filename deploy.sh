#!/usr/bin/env bash

set -e

./gradlew clean test build

ssh relay "sudo killall java" || echo "No Java process running"
ssh relay "sudo rm ~/app/relay.jar" || echo "No Java process running"
ssh relay "docker-compose -f ~/app/docker-compose.prod.yml down" || echo "Docker not running"
scp ./build/libs/relay.jar relay:~/app/
scp ./docker-compose.prod.yml relay:~/app/
ssh relay "docker-compose -f ~/app/docker-compose.prod.yml up -d"
ssh relay "docker-compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh relay "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud --WG_CODE=${WG_CODE} --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} &"