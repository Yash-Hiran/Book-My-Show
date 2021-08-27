package com.demo.book.show.exception

import java.lang.RuntimeException

class ShowNotAvailableException(val code: String) : RuntimeException()
