package com.example.appzonagamer.datasource

import com.example.appzonagamer.model.ApiResponse
import retrofit2.http.GET

interface RestDataSource {

    // Ejemplo: https://randomuser.me/api/?inc=location
    @GET("?inc=location")
    suspend fun getUsuarioLocation(): ApiResponse

    // Ejemplo: https://randomuser.me/api/?inc=picture
    @GET("?inc=picture")
    suspend fun getUsuarioImagen(): ApiResponse
}
