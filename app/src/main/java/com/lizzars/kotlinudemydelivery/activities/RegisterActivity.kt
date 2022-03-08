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
import com.lizzars.kotlinudemydelivery.models.ResponseHttp
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.providers.UsersProvider
import com.lizzars.kotlinudemydelivery.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    var imageViewGoToLogin: ImageView? = null

    var editTextName: EditText? = null
    var editTextLastName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPhone: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConfirmPassword: EditText? = null
    var buttonRegister: Button? = null

    var usersProvider = UsersProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageViewGoToLogin = findViewById(R.id.iv_go_to_login)

        editTextName = findViewById(R.id.et_name)
        editTextLastName = findViewById(R.id.et_last_name)
        editTextEmail = findViewById(R.id.et_email)
        editTextPhone = findViewById(R.id.et_phone)
        editTextPassword = findViewById(R.id.et_password)
        editTextConfirmPassword = findViewById(R.id.et_confirm_password)
        buttonRegister = findViewById(R.id.btn_register)

        imageViewGoToLogin?.setOnClickListener { goToLogin() }

        buttonRegister?.setOnClickListener { register() }
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    /* Metodo para ir al home de cliente */
    private fun goToClienteHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    /* Metodo para ir al guardado de imagen de perfil de usuario */
    private fun goToSaveImage() {
        val i = Intent(this, SaveImageActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    /* Metodo para Registrar */
    private fun register() {
        val name = editTextName?.text.toString()
        val lastName = editTextLastName?.text.toString()
        val email = editTextEmail?.text.toString()
        val phone = editTextPhone?.text.toString()
        val password = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        if (
            isValidatedForm(
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            )
        ) {
            Toast.makeText(this, "El formulario es valido", Toast.LENGTH_SHORT).show()
            val user = User(
                name = name,
                lastname = lastName,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if (response.body()?.success == true) {
                        saveUserInSession(response.body()?.data.toString())
//                        goToClienteHome()
                        goToSaveImage()
                    }

                    Toast.makeText(
                        this@RegisterActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    Log.d(TAG, "Respuesta: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {

                    Log.d(TAG, "Se produjo un error ${t.message}")

                    Toast.makeText(
                        this@RegisterActivity,
                        "Se produjo un error ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }

    }


    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    /* metodo para validar formulario de la actividad */
    private fun isValidatedForm(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isBlank()) {
            Toast.makeText(this, "Debes ingresar el nombre", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastName.isBlank()) {
            Toast.makeText(this, "Debes ingresar el apellidos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.isEmailValid()) {
            Toast.makeText(this, "Debes ingresar el emal valido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isBlank()) {
            Toast.makeText(this, "Debes ingresar el numero de celular", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isBlank()) {
            Toast.makeText(this, "Debes ingresar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }

    /* metodo para guardar datos de usuario en sesion */
    private fun saveUserInSession(data: String) {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //        transformar data en tipo usuario model
        val user = gson.fromJson(data, User::class.java)
        //        almacenando datos en session moblie
        sharedPref.save("user", user)
    }
}