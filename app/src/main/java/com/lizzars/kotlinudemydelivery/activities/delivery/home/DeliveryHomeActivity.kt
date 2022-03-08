package com.lizzars.kotlinudemydelivery.activities.delivery.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.activities.MainActivity
import com.lizzars.kotlinudemydelivery.fragments.client.ClientCategoriesFragment
import com.lizzars.kotlinudemydelivery.fragments.client.ClientOrdersFragment
import com.lizzars.kotlinudemydelivery.fragments.client.ClientProfileFragment
import com.lizzars.kotlinudemydelivery.fragments.delivery.DeliveryOrdersFragment
import com.lizzars.kotlinudemydelivery.fragments.restaurant.RestaurantCategoryFragment
import com.lizzars.kotlinudemydelivery.fragments.restaurant.RestaurantOrdersFragment
import com.lizzars.kotlinudemydelivery.fragments.restaurant.RestaurantProductFragment
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.utils.SharedPref

class DeliveryHomeActivity : AppCompatActivity() {

    private val tag = "DeliveryHomeActivity"

    //    var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null

    /* crear e instanciar para el bottom navigation */
    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)

        sharedPref = SharedPref(this)
//        buttonLogout = findViewById(R.id.btn_logout)
//        buttonLogout?.setOnClickListener { logout() }

        openFragment(DeliveryOrdersFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }



        getUserFromSession()
    }


    /* Metodo para Remover Sesion de mobile */
    private fun logout() {

        sharedPref?.remove("user")

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)


    }

    /* metodo para obtener datos de usuario en sesion del mobile */
    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //            si el usuario existe en sesion
            //    convertir dato en un modelo User
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(tag, "Usuario: $user")
        }
    }

    /* metodo para abrir fragmentos */
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}