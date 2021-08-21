SELECT name, password FROM users
WHERE name = :name AND password = CRYPT(:password, GEN_SALT('bf'));
