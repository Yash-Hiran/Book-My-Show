package com.demo.book.ticket.repository

import com.demo.book.show.entity.Show
import com.demo.book.show.exception.ShowDoesNotExistException
import com.demo.book.ticket.entity.Ticket
import com.demo.book.ticket.request.TicketRequest
import norm.query
import show.GetShowByIdParams
import show.GetShowByIdQuery
import ticket.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class TicketBookingRepository(@Inject private val dataSource: DataSource) {
    fun saveTicket(ticketRequest: TicketRequest, ticketNo: Int) = dataSource.connection.use { connection ->
        run {
            val ticketResult = SaveTicketQuery().query(
                connection,
                SaveTicketParams(ticketRequest.showId, ticketNo, ticketRequest.phoneNo)
            )[0]
            Ticket(
                ticketId = ticketResult.ticketid,
                showId = ticketResult.showid,
                seatNo = ticketResult.seatno,
                phoneNo = ticketResult.phoneno
            )
        }
    }

    fun getAvailableSeatNo(showId: Int): Int = dataSource.connection.use { connection ->
        run {
            val seatNo =
                FindMaximumBookedSeatNumberQuery().query(connection, FindMaximumBookedSeatNumberParams(showId))[0]
            if (seatNo.max != null) seatNo.max + 1 else 1
        }
    }

    fun getShowById(showId: Int) = dataSource.connection.use { connection ->
        run {
            val show = GetShowByIdQuery().query(connection, GetShowByIdParams(showId)).firstOrNull()
                ?: throw ShowDoesNotExistException("com.book.api.repository")
            Show(
                show.id,
                show.movieId,
                show.showDate.toLocalDate(),
                show.startTime.toLocalDateTime(),
                show.endTime.toLocalDateTime(),
                show.capacity
            )
        }
    }
}
