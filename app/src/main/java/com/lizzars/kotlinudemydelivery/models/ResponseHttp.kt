package com.lizzars.kotlinudemydelivery.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class ResponseHttp(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JsonObject,
    @SerializedName("error") val error: String

) {
    override fun toString(): String {
        return "ResponseHttp(success=$success, message='$message', data=$data, error='$error')"
    }
}