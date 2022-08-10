package com.example.yetanothertodolist.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TaskHolder
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.TodoItemRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job


class TodoAdapter(val repository: TodoItemRepository, context: Context) : RecyclerView.Adapter<TaskHolder>() {

    var info: List<TodoItem> = listOf()

    lateinit var containerForSnackBar: View // странно хранить такое в адаптере.
    lateinit var snackbar: Snackbar // Особенно с учётом того, что в самом адаптере оно не используется

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TaskHolder(view, this)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(info[position])
    }

    override fun getItemCount(): Int = info.size



    val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())

    val colorsForImportant = intArrayOf( // и это я бы куда-нибудь унёс. Можно даже в TaskHolder
        context.resources.getColor(R.color.color_green, null),
        context.resources.getColor(R.color.color_red, null)
    )
    val colorsForLowAndBasic = intArrayOf(
        context.resources.getColor(R.color.color_green, null),
        context.resources.getColor(R.color.color_gray, null)
    )

    val map = HashMap<String, Job>() // для метода changeIsCompleted
    // map тоже не должна быть в адаптере. И я бы даже не делал переменную публичной, а сделал бы
    // публичные методы cancelTasks(id) и addTask(id, job)

}
