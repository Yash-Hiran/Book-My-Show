INSERT INTO shows(movie_id, show_date, start_time , end_time)
VALUES (:movie_id, :show_date, :start_time , :end_time)
returning *;
