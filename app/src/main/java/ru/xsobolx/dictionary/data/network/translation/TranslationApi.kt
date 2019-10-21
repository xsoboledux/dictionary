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
                  @Query("key") key: String = "trnsl.1.1.20191010T181702Z.26afbe456058daba.af0db9cf467c55898912e0fdbdf3b6e7d1ee17f6") : Single<TranslationResponse>
}

data class TranslationRequest(
    val text: String,
    val lang: String
)

data class TranslationResponse(
    val code: Int,
    val lang: String,
    val text: List<String>
)