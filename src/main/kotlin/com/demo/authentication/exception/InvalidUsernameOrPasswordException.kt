package com.demo.authentication.exception

import java.lang.RuntimeException

class InvalidUsernameOrPasswordException(val code: String) :
    RuntimeException("Invalid username or password")
