package com.example.yetanothertodolist.di

import androidx.fragment.app.activityViewModels
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.ui.stateholders.AddFragmentViewModel
import com.example.yetanothertodolist.ui.view.addFragment.AddFragment
import com.example.yetanothertodolist.ui.view.addFragment.AddFragmentViewController
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope


@AddFragmentComponentViewScope
@Subcomponent(modules = [AddFragmentComponentViewModule::class])
interface AddFragmentComponentView {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance binding: AddFragmentBinding): AddFragmentComponentView
    }

    val addFragmentViewController: AddFragmentViewController
}

@Scope
annotation class AddFragmentComponentViewScope

@Module
object AddFragmentComponentViewModule{

    @Provides
    @AddFragmentComponentViewScope
    fun viewModel(factory: ViewModelFactory, fragment: AddFragment): AddFragmentViewModel{
        val viewModel: AddFragmentViewModel by fragment.activityViewModels { factory }
        return viewModel
    }
}