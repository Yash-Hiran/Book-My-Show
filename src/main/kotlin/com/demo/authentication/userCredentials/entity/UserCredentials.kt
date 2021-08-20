package com.demo.authentication.userCredentials.entity

data class UserCredentials(
    val id : Int,
    val userName: String,
    val hashedPassword: String
)
