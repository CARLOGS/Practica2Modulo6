package com.dgtic.practica2modulo6.data.remote.model

import com.google.gson.annotations.SerializedName

data class LanguageDto(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("co")
    var co: String? = null,

    @SerializedName("year")
    var year: Int? = null,

    @SerializedName("oop")
    var oop: String? = null,

    @SerializedName("image_url")
    var image_url: String? = null
)
