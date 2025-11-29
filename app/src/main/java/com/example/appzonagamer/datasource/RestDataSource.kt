package com.example.appzonagamer.datasource

import com.example.appzonagamer.model.ApiResponse
import retrofit2.http.GET


interface RestDataSource {

    @GET("?inc=name")
    suspend fun getUsuarioErrores(): ApiResponse

    @GET("?inc=location")
    suspend fun getUsuarioLocation(): ApiResponse

    @GET("?inc=picture")
    suspend fun getUsuarioImagen(): ApiResponse
}
