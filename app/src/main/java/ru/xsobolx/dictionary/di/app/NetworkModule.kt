package ru.xsobolx.dictionary.di.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.xsobolx.dictionary.BuildConfig
import ru.xsobolx.dictionary.data.network.base.ApiKeyInterceptor

@Module
class NetworkModule {
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        return  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideGsonConverter(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideRxCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    fun provideLoggingIntercepter(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}