INSERT INTO shows(movie_id, show_date, start_time , end_time, capacity)
VALUES (:movie_id, :show_date, :start_time , :end_time, :capacity)
returning *;
