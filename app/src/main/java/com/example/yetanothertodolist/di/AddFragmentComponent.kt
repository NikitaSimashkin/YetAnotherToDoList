package com.example.yetanothertodolist.di


import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.ui.view.addFragment.AddFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@AddFragmentComponentScope
@Subcomponent(modules = [AddFragmentComponentModule::class])
interface AddFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: AddFragment): AddFragmentComponent
    }

    fun addFragmentComponentView(): AddFragmentComponentView.Factory

}

@Scope
annotation class AddFragmentComponentScope

@Module
object AddFragmentComponentModule {


    @Provides
    @AddFragmentComponentScope
    @TextviewImportanceAdapter
    fun textView() = R.id.spinner_item

    @Provides
    @AddFragmentComponentScope
    @ResImportanceAdapter
    fun layout() = R.layout.importance_item
}

@Qualifier
annotation class ResImportanceAdapter

@Qualifier
annotation class TextviewImportanceAdapter