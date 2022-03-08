package com.lizzars.kotlinudemydelivery.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.activities.client.home.ClientHomeActivity
import com.lizzars.kotlinudemydelivery.activities.delivery.home.DeliveryHomeActivity
import com.lizzars.kotlinudemydelivery.activities.restaurant.home.RestaurantHomeActivity
import com.lizzars.kotlinudemydelivery.models.ResponseHttp
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.providers.UsersProvider
import com.lizzars.kotlinudemydelivery.utils.SharedPref
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    var tag = "MainActivity"

    /* Instanciar variables ids de actividad */
    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null

    var userProvider = UsersProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Instanciar variables de una vista */
        imageViewGoToRegister = findViewById(R.id.imageview_go_to_register)
        editTextEmail = findViewById(R.id.et_email)
        editTextPassword = findViewById(R.id.et_password)
        buttonLogin = findViewById(R.id.btn_login)

        /* realizar un metodo click a un findView */
        imageViewGoToRegister?.setOnClickListener { goToRegister() }
        buttonLogin?.setOnClickListener { login() }

        getUserFromSession()

    }

    /* Metodo para ir a la vista de Registro */
    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    /* Metodo para ir al home de cliente */
    private fun goToClienteHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    /* Metodo para ir al home de Restaurante */
    private fun goToRestaurantHome() {
        val i = Intent(this, RestaurantHomeActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    /* Metodo para ir al home de Delivery */
    private fun goToDeliveryHome() {
        val i = Intent(this, DeliveryHomeActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    /* Metodo para ir la actividad de seleccionar rol */
    private fun goToSelectRol() {
        val i = Intent(this, SelectRolesActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    /* Metodo que ejecute el login */
    private fun login() {

        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()

        if (isValidatedForm(email, password)) {

            userProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(tag, "Response: ${response.body()}")

                    if (response.body()?.success == true) {
                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        saveUserInSession(response.body()?.data.toString())


                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Los datos no son correctos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(tag, "Hubo un error ${t.message}")

                    Toast.makeText(
                        this@MainActivity,
                        "Hubo un error ${t.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })


        } else {
            Toast.makeText(this, "No es valido", Toast.LENGTH_SHORT).show()
        }

        /*    Log.d("MainActivity", "El email es: $email")
            Log.d("MainActivity", "El password es: $password")*/

    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    /* metodo para validar formulario de la actividad */
    private fun isValidatedForm(email: String, password: String): Boolean {

        if (email.isBlank()) {
            return false
        }

        if (password.isBlank()) {
            return false
        }

        if (!email.isEmailValid()) {
            return false
        }

        return true

    }

    /* metodo para guardar datos en session en mobile caso "user" */
    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //        transformar data en tipo usuario model
        val user = gson.fromJson(data, User::class.java)
        //        almacenando datos en session moblie
        sharedPref.save("user", user)

        if (user.roles?.size!! > 1) {
            // el usuario tiene mas de un rol
            goToSelectRol()

        } else {
            // el usario solo tiene un rol
            goToClienteHome()
        }
    }

    //    obtener datos de usaurio de la sesion
    private fun getUserFromSession() {

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //            si el usuario existe en sesion
            //    convertir dato en un modelo User
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)

            if (!sharedPref.getData("rol").isNullOrBlank()) {
                /* si el usuario seleciono el rol*/
                val rol = sharedPref.getData("rol")?.replace("\"", "")
                Log.d(tag, "ROL $rol")

                if (rol == "RESTAURANTE") {
                    goToRestaurantHome()
                } else if (rol == "CLIENTE") {
                    goToClienteHome()
                } else if (rol == "REPARTIDOR") {
                    goToDeliveryHome()
                }
            } else {
                Log.d(tag, "ROL NO EXISTE")
                goToClienteHome()
            }
        }
    }

}