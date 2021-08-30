package com.demo.book.ticket.exception

import java.lang.RuntimeException

class TicketsUnavailableException(val code: String) : RuntimeException()
