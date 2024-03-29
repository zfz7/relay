# Relay

Relay provides a simple web UI, to distribute [Wiregaurd](https://www.wireguard.com) credentials based off of a
preshared code. It also provides a very simple management panel at `/admin` to see basic info about the service. A live
demo of the project can be found [here](https://relay-demo.zfz7.org). Password is `test1`

For the last decade I have been running various VPN services for family and friends, and have grown tired of manually
managing their credentials via email/[signal](https://signal.org/) etc...

## Goals

- [X] Create a simple Web UI to grant new Wiregaurd VPN credentials
- [X] Create a simple `/admin` control panel with some basic stats and controls
- [X] Simple deployment process using an Ubuntu 20.04 AWS EC2 instant
- [X] Simple deployment process using an Ubuntu 20.04 Digital Ocean droplet
- [X] Mange everything from my phone

## Development Setup

1. Clone this repo `git clone git@github.com:zfz7/relay.git`
2. Install [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/)
3. Install [yarn](https://classic.yarnpkg.com/lang/en/docs/install)
4. Start database `docker-compose up -d`
5. Run the app on port 8080 `./gradlew clean bootrun`
6. Run all tests `./gradlew clean backend:test frontend:test acceptance:test` or `./gradlew clean test`

## Deploy to a single AWS EC2 instance / Digital Ocean droplet

#### Step 1. create an AWS EC2 instance / Digital Ocean droplet

- Create a ubuntu 20.04 LTS AWS EC2 instance / Digital Ocean droplet
- Create and associate elastic IP to that instance
- Point your DNS at that IP address
- Set up inbound firewall rules:

| IP Version | Type  | Protocol                        | Port    | Source    |
|------------|-------|---------------------------------|---------|-----------|
| IPv4       | HTTP  | TCP                             | 80      | 0.0.0.0/0 |
| IPv6       | HTTP  | TCP                             | 80      | ::/0      |
| IPv4       | HTTPS | TCP                             | 443     | 0.0.0.0/0 |
| IPv6       | HTTPS | TCP                             | 443     | ::/0      |
| IPv4       | SSH   | TCP                             | 22      | 0.0.0.0/0 |
| IPv6       | SSH   | TCP                             | 22      | ::/0      |
| IPv4       | UDP   | Custom UDP (Wireguard traffic)  | 51820   | 0.0.0.0/0 |
| IPv6       | UPD   | Custom UDP (Wireguard traffic)  | 51820   | ::/0      |

#### Step 2. create GitHub app (to authenticate `/admin` panel)

- Homepage URL should look like: `https://relay-demo.zfz7.org/`
- Callback URL should look like: `https://relay-demo.zfz7.org/login/oauth2/code/github`
- Create client secret
- Store client id in `GITHUB_PROD_CLIENT_ID` (see [.envrc.example](./.envrc.example))
- Store client secret in `GITHUB_PROD_CLIENT_SECRET` (see [.envrc.example](./.envrc.example))

#### Step 3. setup environment variables 
- Fill in all environment variables in [.envrc.example](./.envrc.example)
- If desired you can use [direnv](https://direnv.net/) for an easy way to manage environment variables
```
RELAY_URL="relay-demo.zfz7.org"
RELAY_WG_PORT="51820"
RELAY_SSH_PORT="22"
RELAY_SSH_USER="ubuntu"

SSL_KEY_STORE_PASSWORD="example"
POSTGRES_DB_PASSWORD="example"
GITHUB_DEV_CLIENT_ID="example"
GITHUB_DEV_CLIENT_SECRET="example"
GITHUB_PROD_CLIENT_ID="example"
GITHUB_PROD_CLIENT_SECRET="example"
```

#### Step 4. setup application.yml variables 
- Ensure [application.yml](./backend/src/main/resources/application.yml) is setup correctly
- Change admin users to your GitHub username
```
relayConfig:
  url: ${RELAY_URL}
  wgPort: ${RELAY_WG_PORT}
  adminUsers: zfz7 
```

#### Step 5. setup AWS EC2  / Digital Ocean instance

- Run [`./setupVM.sh`](./setupVM.sh) or [`./setupVM.sh DO`](./setupVM.sh) on instance 
  - Check the and update to the latest docker compose version
  - `scp -P "$RELAY_SSH_PORT" ./setupVM.sh $RELAY_SSH_USER@$RELAY_URL:~/`

#### Step 6. create letscrypt cert
- run `sudo certbot certonly --standalone` then follow prompts

#### Step 7. deploy app
- `./deploy.sh` or `./deploy.sh NOTEST`
