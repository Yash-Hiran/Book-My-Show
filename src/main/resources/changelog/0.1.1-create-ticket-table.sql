CREATE TABLE tickets (
    ticketId SERIAL PRIMARY KEY,
    showId INTEGER NOT NULL,
    seatNo INTEGER NOT NULL,
    phoneNo INTEGER NOT NULL,
    FOREIGN KEY(showId) REFERENCES shows(id)
);
