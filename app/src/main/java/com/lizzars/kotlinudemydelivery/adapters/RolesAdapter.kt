package com.lizzars.kotlinudemydelivery.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lizzars.kotlinudemydelivery.R
import com.lizzars.kotlinudemydelivery.activities.client.home.ClientHomeActivity
import com.lizzars.kotlinudemydelivery.activities.delivery.home.DeliveryHomeActivity
import com.lizzars.kotlinudemydelivery.activities.restaurant.home.RestaurantHomeActivity
import com.lizzars.kotlinudemydelivery.models.Rol
import com.lizzars.kotlinudemydelivery.utils.SharedPref

class RolesAdapter(val context: Activity, val roles: ArrayList<Rol>) :
    RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)

    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        // nos devolveria un rol por posicion
        val rol = roles[position]

        holder.textViewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener { goToRol(rol) }

    }

    /* metodo para ir a una actividad */
    private fun goToRol(rol: Rol) {
        if (rol.name == "RESTAURANTE") {
            sharedPref.save("rol", rol.name)
            val i = Intent(context, RestaurantHomeActivity::class.java)
            context.startActivity(i)
        } else if (rol.name == "CLIENTE") {
            sharedPref.save("rol", rol.name)
            val i = Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        } else if (rol.name == "REPARTIDOR") {
            sharedPref.save("rol", rol.name)
            val i = Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.tv_rol)
            imageViewRol = view.findViewById(R.id.iv_rol)
        }

    }

}