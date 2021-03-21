CREATE TABLE relay
(
    id         bigserial primary key,
    address VARCHAR(255) not null,
    listen_port VARCHAR(255) not null,
    private_key VARCHAR(255) not null,
    public_key VARCHAR(255) not null
);