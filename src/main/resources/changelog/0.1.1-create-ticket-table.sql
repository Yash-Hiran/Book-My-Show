CREATE TABLE tickets (
    ticketId SERIAL PRIMARY KEY,
    showId INTEGER NOT NULL,
    customerId INTEGER NOT NULL,
    seatNo INTEGER NOT NULL,
    FOREIGN KEY(showId) REFERENCES shows(id),
    FOREIGN KEY(customerId) REFERENCES customers(customerId)
);
