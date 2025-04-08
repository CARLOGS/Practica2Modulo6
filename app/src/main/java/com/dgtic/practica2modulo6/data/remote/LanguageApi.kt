package com.dgtic.practica2modulo6.data.remote

import com.dgtic.practica2modulo6.data.remote.model.LanguageDetailDto
import com.dgtic.practica2modulo6.data.remote.model.LanguageDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Carlo García Sánchez
 *
 * Lista:
 * https://private-f15034-carlogs1.apiary-mock.com/languages
 *
 * Detalle:
 * https://private-f15034-carlogs1.apiary-mock.com/language/1
 */
interface LanguageApi {
    @GET("languages")
    suspend fun getLanguages(): List<LanguageDto>

    @GET("language/{id}")
    suspend fun getLanguageDetail(
        @Path("id") id: String?
    ): LanguageDetailDto
}