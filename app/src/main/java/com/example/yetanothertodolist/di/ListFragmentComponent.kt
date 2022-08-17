package com.example.yetanothertodolist.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope


@ListFragmentComponentScope
@Subcomponent(modules = [ListFragmentComponentModule::class])
interface ListFragmentComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance fragment: Fragment): ListFragmentComponent
    }

    fun listFragmentComponentView(): ListFragmentComponentView.Factory
}

@Scope
annotation class ListFragmentComponentScope

@Module
object ListFragmentComponentModule{

    @Provides // тут тебе не нужно, чтобы даггер хранил ссылку в компоненте
    fun viewModel(fragment: Fragment, factory: ViewModelFactory):ListFragmentViewModel{
        val model: ListFragmentViewModel by fragment.activityViewModels { factory }
        return model
    }
}
