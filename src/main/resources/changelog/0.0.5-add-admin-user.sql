INSERT INTO users (username, password) VALUES ('admin', CRYPT('Crypt0n', GEN_SALT('bf')));
