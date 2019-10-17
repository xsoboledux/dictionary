package ru.xsobolx.dictionary.data.network.translation
//
//import io.reactivex.Single
//import retrofit2.Retrofit
//import javax.inject.Inject
//
//interface TranslationNetworkDataSource {
//
//    fun translate(word: String, lang: String): Single<TranslationResponse>
//
//    class Impl
//    @Inject constructor(private val retrofit: Retrofit) : TranslationNetworkDataSource {
//        override fun translate(word: String, lang: String): Single<TranslationResponse> {
//            val translationApi = retrofit.create(TranslationApi::class.java)
//            return translationApi.translate()
//        }
//    }
//}