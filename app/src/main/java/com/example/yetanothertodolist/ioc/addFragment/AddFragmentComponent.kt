package com.example.yetanothertodolist.ioc.addFragment

import android.content.Context
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.ioc.ApplicationComponent
import com.example.yetanothertodolist.ui.view.addFragment.AddFragment
import com.example.yetanothertodolist.ui.view.addFragment.ImportanceAdapter

/**
 * Компонент фрагмента добавления, создан только ради адаптера
 */
class AddFragmentComponent(
    context: Context,
    val fragment: AddFragment,
    val applicationComponent: ApplicationComponent
) {
    val importanceAdapter = ImportanceAdapter(context, R.layout.importance_item, R.id.spinner_item)
}