package com.example.yetanothertodolist

import android.app.Application
import com.example.yetanothertodolist.Backend.YetAnotherAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YetAnotherApplication : Application() {

    lateinit var repository: TodoItemRepository
        private set

    var yetAnotherAPI: YetAnotherAPI = createApi()
        private set

    override fun onCreate() {
        super.onCreate()

        createRepository()
        createApi()
    }

    private fun createApi(): YetAnotherAPI {
        val token = "ShandalarRokas"
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authorizationInterceptor = Interceptor.invoke {
            val header =
                it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            it.proceed(header)
        }


        val okHttpClient = OkHttpClient.Builder().addInterceptor(logInterceptor).addInterceptor(authorizationInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yetAnotherAPI = retrofit.create(YetAnotherAPI::class.java)

        return yetAnotherAPI
    }

    private fun createRepository() {
        repository = TodoItemRepository(yetAnotherAPI)
    }
}