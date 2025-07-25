package com.example.shift_e.model

data class UserData(
    val firstName: String = "",
    val lastName: String = "",
    val birthday: String = "",
    val email: String = "Loading...",
    val mobile: String = "Loading...",
    val totalRides: Int = 0,
    val moneySaved: Int = 0,
    val rating: Double = 0.0
)
