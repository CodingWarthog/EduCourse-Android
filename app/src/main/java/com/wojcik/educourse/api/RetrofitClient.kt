package com.wojcik.educourse.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


object RetrofitClient {
    private const val BASE_URL = "http://educourseapi.azurewebsites.net/api/"
    var TOKEN = ""

    private val interceptor: HttpLoggingInterceptor =  HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer $TOKEN")
                        .method(original.method(), original.body())

                    val request = requestBuilder.build()
                        chain.proceed(request)
                }

        .addInterceptor(interceptor)
        .build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }


}