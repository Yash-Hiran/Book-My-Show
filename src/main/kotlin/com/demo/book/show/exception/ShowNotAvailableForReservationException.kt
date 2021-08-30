package com.demo.book.show.exception

import java.lang.RuntimeException

class ShowNotAvailableForReservationException(val code: String) : RuntimeException()
