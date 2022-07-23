package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.webservices.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitProviderModule {

    const val THRESHOLD_API_REFRESH_TIME_MINTS=30

    @Singleton
    @Provides
    fun createApi(@ApplicationContext appContext: Context): Api {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val jsonFileUrl = appContext.getString(R.string.baseUrl)
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(logging)
        builder.addInterceptor(Interceptor { chain ->
            val request: Request.Builder = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("app_id", BuildConfig.API_KEY).build()
            request.url(url)
            chain.proceed(request.build())
        })
        val client: OkHttpClient = builder
            .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(jsonFileUrl)
            .build()
            .create(Api::class.java)
    }
}