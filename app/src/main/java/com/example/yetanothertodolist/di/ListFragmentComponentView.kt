package com.example.yetanothertodolist.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentViewController
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@ListFragmentComponentViewScope
@Subcomponent(modules = [ListFragmentComponentViewModule::class])
interface ListFragmentComponentView {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance binding: ListFragmentBinding):ListFragmentComponentView
    }

    val listFragmentViewController: ListFragmentViewController
}

@Scope
annotation class ListFragmentComponentViewScope

@Module
object ListFragmentComponentViewModule{

    @Provides
    fun lifecycleOwner(fragment: Fragment): LifecycleOwner{
        return fragment
    }
}