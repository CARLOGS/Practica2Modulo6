package com.dgtic.practica2modulo6.application

import android.app.Application
import com.dgtic.practica2modulo6.data.LanguageRepository
import com.dgtic.practica2modulo6.data.remote.RetrofitHelper

class Practica2Modulo6App: Application() {
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        LanguageRepository(retrofit)

    }
}