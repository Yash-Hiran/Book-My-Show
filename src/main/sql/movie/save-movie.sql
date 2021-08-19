INSERT INTO movies(title, duration)
VALUES (:title, :duration)
returning *;
