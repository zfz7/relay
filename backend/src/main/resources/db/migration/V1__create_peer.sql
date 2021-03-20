CREATE TABLE peer
(
    id         bigserial primary key,
    public_id  uuid not null,
    expiration timestamptz not null
);