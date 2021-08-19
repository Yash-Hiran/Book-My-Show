ALTER TABLE movies
ADD duration INTEGER;

UPDATE movies
SET duration=EXTRACT(EPOCH FROM (start_time - end_time))/60
