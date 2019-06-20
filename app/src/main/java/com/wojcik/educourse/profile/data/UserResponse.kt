package com.wojcik.educourse.profile.data

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("role")
    val role: String?,
    @SerializedName("email")
    val email: String
)