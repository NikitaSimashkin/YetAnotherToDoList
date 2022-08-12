package com.example.yetanothertodolist.ioc

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.yetanothertodolist.other.UpdateListWorker
import com.example.yetanothertodolist.data.sources.DataSource
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.data.sources.YetAnotherAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Компонент приложения, хранит репозиторий, источник данных, обработчик ошибок и фабрику создания
 * viewModel
 */
class ApplicationComponent(applicationContext: Context) {

    private val yetAnotherAPI: YetAnotherAPI = createApi()

    private val dataSource = DataSource(yetAnotherAPI)

    val repository = TodoItemRepository(dataSource)

    val viewModelFactory = ViewModelFactory(repository)

    init {
        setWorkManager(applicationContext)
    }

    private fun createApi(): YetAnotherAPI {

        val logInterceptor = getLoggingInterceptor()

        val token = "ShandalarRokas"
        val authorizationInterceptor = getAuthInterceptor(token)

        val okHttpClient = getOkHttpClient(logInterceptor, authorizationInterceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(YetAnotherAPI::class.java)
    }

    private fun getOkHttpClient(
        logInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(logInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    private fun getAuthInterceptor(token: String): Interceptor {
        val authorizationInterceptor = Interceptor.invoke {
            val header =
                it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            it.proceed(header)
        }
        return authorizationInterceptor
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }


    private fun setWorkManager(context: Context) {
        val updateListRequest =
            PeriodicWorkRequestBuilder<UpdateListWorker>(8, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueue(updateListRequest)
    }
}