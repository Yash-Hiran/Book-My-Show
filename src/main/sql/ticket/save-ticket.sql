INSERT INTO tickets(showId, seatNo, phoneNo)
VALUES (:showId, :seatNo, :phoneNo)
RETURNING *;
