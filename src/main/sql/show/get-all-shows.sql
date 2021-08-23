SELECT shows.id,shows.show_date, movies.title, shows.start_time, movies.duration FROM shows
INNER JOIN movies
ON movies.id = shows.movie_id
ORDER BY shows.start_time DESC
