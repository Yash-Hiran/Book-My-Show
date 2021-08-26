CREATE TABLE shows (
  id SERIAL PRIMARY KEY,
  movie_id INTEGER NOT NULL,
  show_date DATE NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  end_time TIMESTAMPTZ NOT NULL,
  FOREIGN KEY(movie_id) REFERENCES movies(id)
);
