package ru.xsobolx.dictionary.data.network.translation

import io.reactivex.Single
import retrofit2.http.POST

interface TranslationApi {
    @POST()
    fun translate(text: String, lang: String) : Single<TranslationResponse>
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