package com.example.appzonagamer.di

import android.content.Context
import com.example.appzonagamer.datasource.AppDatabase
import com.example.appzonagamer.datasource.RestDataSource
import com.example.appzonagamer.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataSourceModule {

    lateinit var userRepository: UserRepository
        private set

    fun init(context: Context) {
        // --- ROOM (DB local) ---
        val db = AppDatabase.getInstance(context)
        val usuarioDao = db.usuarioDao()

        // --- Retrofit (API externa RandomUser) ---
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val restDataSource = retrofit.create(RestDataSource::class.java)

        // --- Repositorio único ---
        userRepository = UserRepository(
            usuarioDao = usuarioDao,
            restDataSource = restDataSource
        )
    }
}
