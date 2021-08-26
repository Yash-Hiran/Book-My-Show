package com.demo.authentication.userCredentials.entity

data class UserCredentials(
    val id: Int,
    val username: String,
    val hashedPassword: String
)
