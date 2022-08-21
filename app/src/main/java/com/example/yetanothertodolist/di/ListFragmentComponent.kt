package com.example.yetanothertodolist.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.ListFragment
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentOpenCloseController
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
        fun create(@BindsInstance fragment: ListFragment): ListFragmentComponent
    }

    fun listFragmentComponentView(): ListFragmentComponentView.Factory
    fun listFragmentOpenCloseController(): ListFragmentOpenCloseController
}

@Scope
annotation class ListFragmentComponentScope

@Module
object ListFragmentComponentModule{

    @Provides
    fun viewModel(fragment: Fragment, factory: ViewModelFactory):ListFragmentViewModel{
        val model: ListFragmentViewModel by fragment.activityViewModels { factory }
        return model
    }

    @Provides
    fun fragment(fragment: ListFragment): Fragment{
        return fragment
    }
}