truncate table peer;
truncate table log_event;
truncate table code;

INSERT INTO config (disable_logs, client_valid_duration, created_date)
VALUES (false, 10, NOW());