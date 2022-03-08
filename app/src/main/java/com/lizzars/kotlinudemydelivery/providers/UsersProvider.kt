package com.lizzars.kotlinudemydelivery.providers

import com.lizzars.kotlinudemydelivery.api.ApiRoutes
import com.lizzars.kotlinudemydelivery.models.ResponseHttp
import com.lizzars.kotlinudemydelivery.models.User
import com.lizzars.kotlinudemydelivery.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {

    private var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUserRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return usersRoutes?.register(user)
    }

    fun login(email: String, password: String): Call<ResponseHttp>? {
        return usersRoutes?.login(email, password)
    }
}