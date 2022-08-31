package com.example.yetanothertodolist.ui.view.listFragment.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.di.ListFragmentComponentScope
import com.example.yetanothertodolist.other.getColor
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentAccessibilityController
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentOpenCloseController
import javax.inject.Inject

/**
 * Адаптер для RecyclerView на фрагменте листа, отображает список заданий
 */
@ListFragmentComponentScope
class TodoAdapter @Inject constructor(
    private val viewModel: ListFragmentViewModel,
    context: Context,
    private val listFragmentOpenCloseController: ListFragmentOpenCloseController,
    private val listFragmentAccessibilityController: ListFragmentAccessibilityController
) : RecyclerView.Adapter<TaskHolder>() {

    var info = viewModel.tasks.value ?: listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TaskHolder(view, viewModel, listFragmentOpenCloseController, listFragmentAccessibilityController)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(info[position])
    }

    override fun getItemCount(): Int = info.size

    init {
        states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        colorsForImportant = intArrayOf(
            getColor(context, R.attr.color_green),
            getColor(context, R.attr.color_red)
        )
        colorsForLowAndBasic = intArrayOf(
            getColor(context, R.attr.color_green),
            getColor(context, R.attr.color_gray)
        )
    }

    companion object {

        lateinit var states: Array<IntArray>

        lateinit var colorsForImportant: IntArray

        lateinit var colorsForLowAndBasic: IntArray
    }
}