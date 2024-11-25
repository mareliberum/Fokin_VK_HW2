package com.example.hw2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(       //Объект присылаемый response
    @SerialName("data")
    val data: List<GifObject>,
    @SerialName("pagination")
    val pagination: Pagination

)

@Serializable
data class GifObject(
    @SerialName("id")
    val id: String,
    @SerialName("images")
    val images: Images
)

@Serializable
data class Pagination(
    @SerialName("total_count")
    val total_count: Int,
    @SerialName("count")
    val count: Int,
    @SerialName("offset")
    val offset: Int
)

@Serializable
data class Images(
    @SerialName("original")
    val original: Original
)


@Serializable
data class Original(
    @SerialName("url")
    val url: String
)