package ru.xsobolx.dictionary.data.network.base

import okhttp3.Interceptor
import okhttp3.Response
import ru.xsobolx.dictionary.BuildConfig
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()
        val newRequest = request.newBuilder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}