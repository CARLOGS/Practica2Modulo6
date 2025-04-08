package com.dgtic.practica2modulo6.data.remote

import com.dgtic.practica2modulo6.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    // Para interceptar peticiones y respuestas en el Body
    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }.build()

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client) // NOTA: Para interceptar peticiones y respuestas en el Body (se agregó después)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}