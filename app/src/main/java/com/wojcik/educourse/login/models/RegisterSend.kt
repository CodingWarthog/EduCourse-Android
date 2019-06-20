package com.wojcik.educourse.login.models

import com.google.gson.annotations.SerializedName

data class  RegisterSend (
    @SerializedName("Username")
    val username: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("FirstName")
    val firstname: String,
    @SerializedName("LastName")
    val lastname: String
)