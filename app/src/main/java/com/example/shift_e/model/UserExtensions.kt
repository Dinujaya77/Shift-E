package com.example.shift_e.model

import com.example.shift_e.R

fun UserData.getProfileImageRes(): Int {
    return when (firstName.lowercase()) {
        "chamika" -> R.drawable.profile_male
        "sakura" -> R.drawable.profile_picture
        "jessica" -> R.drawable.ic_profile
        "iman" -> R.drawable.profile_male
        else -> R.drawable.default_profile_image
    }
}
