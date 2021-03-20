# Relay
Wiregaurd traffic relay 
## Goals
- [X] Code scan using CodeQL which block deployment

## Dev Setup
Will run relay.jar on local machine and DB inside docker
1. Install Docker and Docker Compose
2. Create Docker database, run from app home
    * exposes database on port 5432
    * exposes app on port 8080
```
 docker-compose up -d
```

## EC2 Setup
1. Install Docker, Docker-Compose, openJDK, yarn and certbot
   * If on Ubuntu 20 run from project root: `./setupEC2.sh`
2. Install certs
   * run `certbot certonly --standalone`
3. Create keystore.p12, 
   * run from letscrypt folder: `openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root`
   * Use password `relay`
4. Build Jar
   * run from project root:`./gradlew clean assemble`
5. Start all containers
   * run from project root: `docker-compose -f docker-compose.prod.yml up -d`
   * database is not exposed to localhost
   * exposes app on port 80 and 443
6. Start app
   * run from project root: `java -jar build/libs/relay.jar --spring.profiles.active=cloud`