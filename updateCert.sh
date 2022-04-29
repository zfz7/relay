#!/usr/bin/env bash

set -e

sshHost="-p $RELAY_SSH_PORT -C $RELAY_SSH_USER@$RELAY_URL"

git update-index --refresh
git diff-index --quiet HEAD --

ssh $sshHost "sudo certbot certificates"
ssh $sshHost "sudo certbot certonly --standalone -d $RELAY_URL -n"
ssh $sshHost "sudo openssl pkcs12 -export -in /etc/letsencrypt/live/$RELAY_URL/fullchain.pem -inkey /etc/letsencrypt/live/$RELAY_URL/privkey.pem -out /etc/letsencrypt/live/$RELAY_URL/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/$RELAY_URL/chain.pem -caname root -passout pass:$SSL_KEY_STORE_PASSWORD"
ssh $sshHost "sudo killall java" || echo "No Java process running"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml up -d"
ssh $sshHost "RELAY_URL=${RELAY_URL} POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} RELAY_WG_PORT=${RELAY_WG_PORT} docker compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh $sshHost "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud \
        --RELAY_URL=${RELAY_URL} \
        --RELAY_WG_PORT=${RELAY_WG_PORT} \
        --GITHUB_PROD_CLIENT_ID=${GITHUB_PROD_CLIENT_ID} \
        --GITHUB_PROD_CLIENT_SECRET=${GITHUB_PROD_CLIENT_SECRET} \
        --SSL_KEY_STORE_PASSWORD=${SSL_KEY_STORE_PASSWORD} \
        --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} &"