package com.example.appzonagamer.di

import android.content.Context
import com.example.appzonagamer.datasource.AppDatabase
import com.example.appzonagamer.datasource.RestDataSource
import com.example.appzonagamer.repository.UserRepository


object DataSourceModule {

    lateinit var userRepository: UserRepository
        private set

    fun init(context: Context) {
        // --- ROOM ---
        val db = AppDatabase.getInstance(context)
        val usuarioDao = db.usuarioDao()



    }
}
