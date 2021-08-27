package com.demo.book.show.exception

import java.lang.RuntimeException

class ShowDoesNotExistException(val code: String) : RuntimeException()
