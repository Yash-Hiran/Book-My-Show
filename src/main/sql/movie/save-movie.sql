INSERT INTO movies(title, start_time, end_time)
VALUES (:title, :start_time, :end_time)
returning *;
