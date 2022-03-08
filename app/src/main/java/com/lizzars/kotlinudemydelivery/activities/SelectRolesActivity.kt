package com.lizzars.kotlinudemydelivery.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.adapters.RolesAdapter
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {
    var recyclerViewRoles: RecyclerView? = null
    var user: User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.rv_roles)

        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this, user?.roles!!)
        recyclerViewRoles?.adapter = adapter


    }

    /* obtener los datos de sesion de usaurios*/
    private fun getUserFromSession() {

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //            si el usuario existe en sesion
            //    convertir dato en un modelo User
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}