CREATE EXTENSION pgcrypto;

INSERT INTO users (name, password)
VALUES ('mihir', CRYPT('12345', GEN_SALT('bf'))),
('yash', CRYPT('78910', GEN_SALT('bf')));
