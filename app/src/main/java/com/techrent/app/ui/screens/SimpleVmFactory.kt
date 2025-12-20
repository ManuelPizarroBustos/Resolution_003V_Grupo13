package com.techrent.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimpleVmFactory<T : ViewModel>(private val creator: () -> T) : ViewModelProvider.Factory {
    override fun <R : ViewModel> create(modelClass: Class<R>): R = creator() as R
}
