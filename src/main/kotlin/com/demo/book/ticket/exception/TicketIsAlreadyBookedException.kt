package com.demo.book.ticket.exception

import java.lang.RuntimeException

class TicketIsAlreadyBookedException(val code: String) : RuntimeException()
