#!/usr/bin/env bash

set -e

sshHost="-p $RELAY_SSH_PORT -C $RELAY_SSH_USER@$RELAY_URL"

git update-index --refresh
git diff-index --quiet HEAD --

ssh $sshHost "sudo certbot certificates"
ssh $sshHost "sudo certbot certonly --standalone -d relay.zfz7.org -n"
ssh $sshHost "sudo openssl pkcs12 -export -in /etc/letsencrypt/live/relay.zfz7.org/fullchain.pem -inkey /etc/letsencrypt/live/relay.zfz7.org/privkey.pem -out /etc/letsencrypt/live/relay.zfz7.org/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/relay.zfz7.org/chain.pem -caname root -passout pass:relay"
ssh $sshHost "sudo killall java" || echo "No Java process running"
ssh $sshHost "docker-compose -f ~/app/docker-compose.prod.yml up -d"
ssh $sshHost "docker-compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh $sshHost "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud \
        --WG_CODE=${WG_CODE} \
        --RELAY_URL=${RELAY_URL} \
        --RELAY_WG_PORT=${RELAY_WG_PORT} \
        --GITHUB_PROD_CLIENT_ID=${GITHUB_PROD_CLIENT_ID} \
        --GITHUB_PROD_CLIENT_SECRET=${GITHUB_PROD_CLIENT_SECRET} \
        --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD} &"