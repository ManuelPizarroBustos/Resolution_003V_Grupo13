package com.example.appzonagamer.datasource

import com.example.appzonagamer.model.ApiResponse


interface RestDataSource {

    @GET("?inc=location")
    suspend fun getUsuarioLocation(): ApiResponse


    @GET("?inc=picture")
    suspend fun getUsuarioImagen(): ApiResponse
}

annotation class GET(val value: String)
