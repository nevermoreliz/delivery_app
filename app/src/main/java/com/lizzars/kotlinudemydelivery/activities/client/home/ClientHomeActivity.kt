package com.lizzars.kotlinudemydelivery.activities.client.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.utils.SharedPref

class ClientHomeActivity : AppCompatActivity() {

    private val tag = "ClientHomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        getUserFromSession()
    }

    private fun getUserFromSession() {

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
//            si el usuario existe en sesion
//    convertir dato en un modelo User
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            Log.d(tag, "Usuario: $user")
        }
    }
}