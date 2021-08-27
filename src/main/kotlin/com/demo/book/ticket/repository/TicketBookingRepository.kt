package com.demo.book.ticket.repository

import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.request.TicketRequest
import norm.query
import ticket.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class TicketBookingRepository(@Inject private val dataSource: DataSource) {
    fun saveTicket(ticketRequest: TicketRequest) = dataSource.connection.use { connection ->
        run {
            val ticketResult = SaveTicketQuery().query(
                connection,
                SaveTicketParams(ticketRequest.showId, ticketRequest.seatNo, ticketRequest.phoneNo)
            )[0]
            Ticket(
                ticketId = ticketResult.ticketid,
                showId = ticketResult.showid,
                seatNo = ticketResult.seatno,
                phoneNo = ticketResult.phoneno
            )
        }
    }

    fun isTicketBooked(ticketRequest: TicketRequest) = dataSource.connection.use { connection ->
        run {
            val result = CheckDuplicateTicketQuery().query(
                connection,
                CheckDuplicateTicketParams(ticketRequest.showId, ticketRequest.seatNo)
            )[0]
            result.count != 0L
        }
    }
}
