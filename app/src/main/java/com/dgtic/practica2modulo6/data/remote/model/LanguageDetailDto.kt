package com.dgtic.practica2modulo6.data.remote.model

import com.google.gson.annotations.SerializedName

data class LanguageDetailDto(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("year")
    var year: Int? = null,

    @SerializedName("co")
    var co: String? = null,

    @SerializedName("compiled")
    var compiled: String? = null,

    @SerializedName("cross_platform")
    var cross_platform: String? = null,

    @SerializedName("oop")
    var oop: String? = null,

    @SerializedName("image_url")
    var image_url: String? = null,

    @SerializedName("attributes")
    var attributes: List<String>? = null,

    @SerializedName("youtube")
    var youtube: String? = null
)
