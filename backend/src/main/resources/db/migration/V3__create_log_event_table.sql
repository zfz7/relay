CREATE TABLE log_event
(
    id         bigserial primary key,
    created_date timestamptz not null,
    message VARCHAR(255) not null,
    key1 VARCHAR(255) not null,
    log_type VARCHAR(255) not null
);