#!/usr/bin/env bash

set -e

ssh relay "sudo certbot certificates"
ssh relay "sudo certbot certonly --standalone -d relay.zfz7.org -n"
ssh relay "sudo openssl pkcs12 -export -in /etc/letsencrypt/live/relay.zfz7.org/fullchain.pem -inkey /etc/letsencrypt/live/relay.zfz7.org/privkey.pem -out /etc/letsencrypt/live/relay.zfz7.org/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/relay.zfz7.org/chain.pem -caname root -passout pass:relay"
ssh relay "sudo killall java" || echo "No Java process running"
ssh relay "docker-compose -f ~/app/docker-compose.prod.yml up -d"
ssh relay "docker-compose -f ~/app/docker-compose.prod.yml restart wireguard"
ssh relay "cd ~/app && sudo java -jar ./relay.jar --spring.profiles.active=cloud --WG_CODE=${WG_CODE} --POSTGRES_DB_PASSWORD=${POSTGRES_DB_PASSWORD}&"