package com.demo.book.movie.exception

import java.lang.RuntimeException

class InvalidShowDetailsException(override val message: String, val errorCode: String = "com.api.booking.service") :
    RuntimeException(message)
