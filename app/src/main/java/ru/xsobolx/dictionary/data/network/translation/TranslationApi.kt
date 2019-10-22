package ru.xsobolx.dictionary.data.network.translation

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationApi {
    @GET("/api/v1.5/tr.json/translate?")
    fun translate(@Query("lang") lang: String,
                  @Query("text") text: String,
                  @Query("key") key: String) : Single<TranslationResponse>
}

data class TranslationResponse(
    val code: Int,
    val lang: String,
    val text: List<String>
)