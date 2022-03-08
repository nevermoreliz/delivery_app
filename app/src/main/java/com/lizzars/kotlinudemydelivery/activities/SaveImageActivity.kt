package com.lizzars.kotlinudemydelivery.activities

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.activities.client.home.ClientHomeActivity
import com.lizzars.kotlinudemydelivery.models.ResponseHttp
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.providers.UsersProvider
import com.lizzars.kotlinudemydelivery.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
    val tag = "SaveImageActivity"

    var circleImageViewUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null

    private var imageFile: File? = null

    var userProvider = UsersProvider()

    var user: User? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)

        getUserFromSession()

        circleImageViewUser = findViewById(R.id.civ_user)
        buttonConfirm = findViewById(R.id.btn_confirm)
        buttonNext = findViewById(R.id.btn_next)

        circleImageViewUser?.setOnClickListener { selectImage() }

        buttonNext?.setOnClickListener { goToClienteHome() }
        buttonConfirm?.setOnClickListener { saveImage() }
    }

    /* Metodo para ir al home de cliente */
    private fun goToClienteHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }


    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                /* el archivo que vamos a guardar como imagen en el serividor o storage */
                imageFile = File(fileUri?.path)

                circleImageViewUser?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "La tarea se cancelo.", Toast.LENGTH_SHORT).show()
            }

        }

    /* metodo para seleccionar imagen desde dispositivo mobile */
    private fun selectImage() {
        ImagePicker
            .with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

    /* disparar el metodo de actualizado */
    private fun saveImage() {
        if (imageFile != null && user != null) {

            userProvider.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {

                    Log.d(tag, "Response: $response")
                    Log.d(tag, "Body: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {

                    Log.d(tag, "Error: ${t.message}")
                    Toast.makeText(
                        this@SaveImageActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })

        } else {
            Toast.makeText(
                this,
                "La imagen no puede ser nula ni tampoco los datos de sesion del usuario.",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    /* metodo para obtener datos de usuario en sesion del mobile */
    private fun getUserFromSession() {

        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //            si el usuario existe en sesion
            //    convertir dato en un modelo User
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    /* metodo para guardar datos en session en mobile caso "user" */
    private fun saveUserInSession(data: String) {
        val gson = Gson()
        //        transformar data en tipo usuario model
        val user = gson.fromJson(data, User::class.java)
        //        almacenando datos en session moblie
        sharedPref?.save("user", user)

        goToClienteHome()

    }
}