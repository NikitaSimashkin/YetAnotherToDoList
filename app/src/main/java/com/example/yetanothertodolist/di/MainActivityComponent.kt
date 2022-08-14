package com.example.yetanothertodolist.di

import android.content.Context
import androidx.activity.viewModels
import com.example.yetanothertodolist.other.ErrorManager
import com.example.yetanothertodolist.ui.stateholders.MainActivityViewModel
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.example.yetanothertodolist.ui.view.MainActivity.SnackBarErrorManager
import dagger.*
import javax.inject.Scope


@MainActivityComponentScope
@Subcomponent(modules = [MainActivityComponentModule::class])
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance activity: MainActivity
        ): MainActivityComponent
    }

    fun listFragmentComponent(): ListFragmentComponent.Factory
    fun addFragmentComponent(): AddFragmentComponent.Factory

    fun errorManager(): SnackBarErrorManager
}

@Scope
annotation class MainActivityComponentScope

@Module
interface MainActivityComponentModule {

    @Binds
    @MainActivityComponentScope
    fun errorManager(em: SnackBarErrorManager): ErrorManager

    companion object {
        @Provides
        @MainActivityComponentScope
        fun viewModel(
            activity: MainActivity,
            viewModelFactory: ViewModelFactory
        ): MainActivityViewModel {
            val viewModel by activity.viewModels<MainActivityViewModel> { viewModelFactory }
            return viewModel
        }

        @Provides
        @MainActivityComponentScope
        fun context(activity: MainActivity): Context {
            return activity
        }
    }
}