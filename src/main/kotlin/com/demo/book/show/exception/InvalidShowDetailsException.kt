package com.demo.book.show.exception

import java.lang.RuntimeException

class InvalidShowDetailsException(override val message: String, val errorCode: String = "com.api.booking.service") :
    RuntimeException(message)
