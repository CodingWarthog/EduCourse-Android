package com.wojcik.educourse.login.models

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("token")
    val token: String,
    @SerializedName("Role")
    val Role: String
)