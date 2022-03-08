package com.lizzars.kotlinudemydelivery.fragments.client

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.activities.MainActivity
import com.lizzars.kotlinudemydelivery.activities.SelectRolesActivity
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView


class ClientProfileFragment : Fragment() {

    var myView: View? = null

    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null

    var circleImageView: CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textViewPhone: TextView? = null

    var imageViewLogout: ImageView? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        buttonSelectRol = myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)

        circleImageView = myView?.findViewById(R.id.civ_user)

        textViewName = myView?.findViewById(R.id.tv_name)
        textViewEmail = myView?.findViewById(R.id.tv_email)
        textViewPhone = myView?.findViewById(R.id.tv_phone)

        imageViewLogout = myView?.findViewById(R.id.iv_logout)

        buttonSelectRol?.setOnClickListener { goToSelectRol() }
        imageViewLogout?.setOnClickListener { logout() }

        getUserFromSession()

        textViewName?.text = "${user?.name} ${user?.lastname}"
        textViewEmail?.text = user?.email
        textViewPhone?.text = user?.phone

        /* para mostra una imagen po http */
        if (!user?.image.isNullOrBlank()) {
            Glide.with(requireContext()).load(user?.image).into(circleImageView!!)
        }

        return myView
    }


    /* Metodo para ir la actividad de seleccionar rol */
    private fun goToSelectRol() {
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        /* eliminar historial de pantallas */
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
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

    /* Metodo para Remover Sesion de mobile */
    private fun logout() {

        sharedPref?.remove("user")

        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)

    }

}