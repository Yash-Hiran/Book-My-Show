SELECT username FROM users
WHERE username = :username AND password = CRYPT(:password, password);
