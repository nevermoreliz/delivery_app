package com.lizzars.kotlinudemydelivery.api

import com.lizzars.kotlinudemydelivery.routes.UsersRoutes

class ApiRoutes {

    val API_URL = "http://192.168.100.36:3005/api/"
    val retrofit = RetrofitClient()

    fun getUserRoutes(): UsersRoutes {
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }
}