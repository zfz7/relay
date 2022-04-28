#!/usr/bin/env bash

set -e

git update-index --refresh
git diff-index --quiet HEAD --

sshHost="-p $RELAY_SSH_PORT -C $RELAY_SSH_USER@$RELAY_URL"
scpHost="$RELAY_SSH_USER@$RELAY_URL"

docker-compose up -d
./gradlew clean test build

ssh $sshHost "sudo killall java" || echo "No Java process running"
ssh $sshHost "sudo rm ~/app/relay.jar" || echo "No Java process running"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml pull"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml down" || echo "Docker not running"
scp -P "$RELAY_SSH_PORT" ./build/libs/relay.jar $scpHost:~/app/
scp -P "$RELAY_SSH_PORT" ./docker-compose.prod.yml $scpHost:~/app/
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml up -d --remove-orphans"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh $sshHost "docker image prune -f"
ssh $sshHost "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud \
        --RELAY_URL=${RELAY_URL} \
        --RELAY_WG_PORT=${RELAY_WG_PORT} \
        --GITHUB_PROD_CLIENT_ID=${GITHUB_PROD_CLIENT_ID} \
        --GITHUB_PROD_CLIENT_SECRET=${GITHUB_PROD_CLIENT_SECRET} \
        --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} &"