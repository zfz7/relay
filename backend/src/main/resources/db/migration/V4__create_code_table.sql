CREATE TABLE code
(
    id           bigserial primary key,
    code         VARCHAR(255) not null,
    created_date timestamptz  not null,
    valid_until  timestamptz  null
);