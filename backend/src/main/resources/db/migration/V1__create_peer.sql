CREATE TABLE peer
(
    id         bigserial primary key,
    public_id  uuid not null,
    expiration timestamptz not null,
    address VARCHAR(255) not null,
    dns VARCHAR(255) not null,
    private_key VARCHAR(255) not null,
    allowed_ips VARCHAR(255) not null,
    end_point VARCHAR(255) not null,
    pre_shared_key VARCHAR(255) not null,
    public_key VARCHAR(255) not null
);