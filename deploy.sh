#!/usr/bin/env bash

set -e

git update-index --refresh
git diff-index --quiet HEAD --

sshHost="-p $RELAY_SSH_PORT -C $RELAY_SSH_USER@$RELAY_URL"
scpHost="$RELAY_SSH_USER@$RELAY_URL"

if [ "$1" == 'NOTEST' ]; then
	./gradlew clean build -x test
else
	./gradlew clean test build
fi

ssh $sshHost "mkdir -p ~/app/config"
ssh $sshHost "sudo openssl pkcs12 -export -in /etc/letsencrypt/live/$RELAY_URL/fullchain.pem -inkey /etc/letsencrypt/live/$RELAY_URL/privkey.pem -out /etc/letsencrypt/live/$RELAY_URL/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/$RELAY_URL/chain.pem -caname root -passout pass:$SSL_KEY_STORE_PASSWORD"

ssh $sshHost "sudo killall java" || echo "No Java process running"
ssh $sshHost "sudo rm ~/app/relay.jar" || echo "No Java process running"
scp -P "$RELAY_SSH_PORT" ./docker-compose.prod.yml $scpHost:~/app/
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml pull" || echo "Docker compose not yet started"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml down" || echo "Docker not running"
scp -P "$RELAY_SSH_PORT" ./build/libs/relay.jar $scpHost:~/app/
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml up -d --remove-orphans"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh $sshHost "docker image prune -f"
ssh $sshHost "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud \
        --RELAY_URL=${RELAY_URL} \
        --RELAY_WG_PORT=${RELAY_WG_PORT} \
        --GITHUB_PROD_CLIENT_ID=${GITHUB_PROD_CLIENT_ID} \
        --GITHUB_PROD_CLIENT_SECRET=${GITHUB_PROD_CLIENT_SECRET} \
        --SSL_KEY_STORE_PASSWORD=${SSL_KEY_STORE_PASSWORD} \
        --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} &"