CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (username, password)
VALUES ('admin', CRYPT('crypt0n', GEN_SALT('bf')));
