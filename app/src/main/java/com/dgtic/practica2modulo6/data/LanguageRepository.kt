package com.dgtic.practica2modulo6.data

import com.dgtic.practica2modulo6.data.remote.LanguageApi
import com.dgtic.practica2modulo6.data.remote.model.LanguageDetailDto
import com.dgtic.practica2modulo6.data.remote.model.LanguageDto
import retrofit2.Retrofit

class LanguageRepository(
    private val retrofit: Retrofit
) {
    // Crea instancia del servicio de retrofit LanguageApi
    private val languageApi = retrofit.create(LanguageApi::class.java)

    suspend fun getLanguages(): List<LanguageDto> = languageApi.getLanguages()
    suspend fun getLanguageDetail(id: Int?): LanguageDetailDto = languageApi.getLanguageDetail(id.toString())
}