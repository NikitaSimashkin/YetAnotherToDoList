package com.example.yetanothertodolist.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.data.room.TaskDatabase
import com.example.yetanothertodolist.data.room.TasksDao
import com.example.yetanothertodolist.data.sources.YetAnotherAPI
import com.example.yetanothertodolist.other.ConstValues
import com.example.yetanothertodolist.util.WorkManagerCreator
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class ApplicationScope

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    val repository: TodoItemRepository

    val viewModelFactory: ViewModelFactory

    val mainActivityComponent: MainActivityComponent.Factory

    val worker: WorkManagerCreator

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}


@Module
object ApplicationModule {

    @Provides
    @ApplicationScope
    fun sharedPreference(application: Application): SharedPreferences{
        return application.getSharedPreferences(ConstValues.SHARED_PREF_FILE_NAME, MODE_PRIVATE)
    }

    @Provides
    @ApplicationScope
    fun dao(database: TaskDatabase): TasksDao{
        return database.getDao()
    }

    @Provides
    @ApplicationScope
    fun database(context: Application): TaskDatabase{
        val database: TaskDatabase by lazy{
            Room.databaseBuilder(context, TaskDatabase::class.java, "database.db").build()
        }
        return database
    }

    @Provides
    @ApplicationScope
    fun worker(context: Application): WorkManagerCreator {
        return WorkManagerCreator(context)
    }

    @Provides
    @ApplicationScope
    fun yetAnotherAPI(
        okHttpClient: OkHttpClient
    ): YetAnotherAPI {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(YetAnotherAPI::class.java)
    }

    @Provides
    fun okHttpClient(
        logInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(logInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    fun authInterceptor(@Token token: String): Interceptor {
        val authorizationInterceptor = Interceptor.invoke {
            val header =
                it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            it.proceed(header)
        }
        return authorizationInterceptor
    }

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return logInterceptor
    }

    @Provides
    @Token
    fun token(): String = "ShandalarRokas"

    @Qualifier
    annotation class Token
}