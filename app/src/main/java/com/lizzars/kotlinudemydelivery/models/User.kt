package com.lizzars.kotlinudemydelivery.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("image") val image: String? = null,
    @SerializedName("password") val password: String,
    @SerializedName("is_available") val is_available: Boolean? = null,
    @SerializedName("session_token") val sessionToken: String? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null

) {
    override fun toString(): String {
        return "User(id=$id, email='$email', name='$name', lastname='$lastname', phone='$phone', image=$image, password='$password', is_available=$is_available, sessionToken=$sessionToken, roles=$roles)"
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}