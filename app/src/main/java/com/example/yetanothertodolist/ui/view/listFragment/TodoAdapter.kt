package com.example.yetanothertodolist.ui.view.listFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.ui.stateholders.Action
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import kotlinx.coroutines.*

/**
 * Адаптер для RecyclerView на фрагменте листа, отображает список заданий
 */
class TodoAdapter(
    private val viewModel: ListFragmentViewModel,
    context: Context
) : RecyclerView.Adapter<TaskHolder>() {

    var info = viewModel.tasks.value ?: listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TaskHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(info[position])
    }

    override fun getItemCount(): Int = info.size

    /**
     * Не вижу смысла в каждом из Holder'ов хранить одинаковые объекты, проще
     * положить все это в класс, который управляет всеми Holder'ами
     */
    init {
        states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
        colorsForImportant = intArrayOf(
            context.resources.getColor(R.color.color_green, null),
            context.resources.getColor(R.color.color_red, null)
        )
        colorsForLowAndBasic = intArrayOf(
            context.resources.getColor(R.color.color_green, null),
            context.resources.getColor(R.color.color_gray, null)
        )
    }

    companion object {

        lateinit var states: Array<IntArray>

        lateinit var colorsForImportant: IntArray

        lateinit var colorsForLowAndBasic: IntArray
    }
}