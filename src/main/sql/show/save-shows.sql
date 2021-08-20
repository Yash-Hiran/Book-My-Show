INSERT INTO shows(title, duration)
VALUES (:title, :duration)
returning *;
