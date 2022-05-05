CREATE TABLE config
(
    id                    bigserial primary key,
    disable_logs          boolean     not null,
    client_valid_duration integer     not null,
    created_date          timestamptz not null,
    valid_until           timestamptz null
);

INSERT INTO config (disable_logs, client_valid_duration, created_date)
VALUES (false, 10, NOW());