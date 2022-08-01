package com.example.yetanothertodolist.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.ListFragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TodoItemBinding
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.time.temporal.Temporal

enum class Importance {
    Low, Basic, Important;

    companion object {
        fun getImportance(i: Int): Importance = when (i) {
            0 -> Low
            1 -> Basic
            2 -> Important
            else -> throw IllegalArgumentException()
        }
    }
}

data class TodoItem(
    val id: String,
    val description: String,
    val importance: Importance,
    val isCompleted: Boolean,
    val dateOfCreation: Temporal,
    val deadline: Temporal? = null,
    val dateOfChange: Temporal? = null
) : Serializable

class MyDiffUtill(val oldList: List<TodoItem>, val newList: List<TodoItem>): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old == new
    }

}

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TaskHolder>() {
    var counter = 0

    var info: List<TodoItem> = ArrayList(ListFragment.repository.tasks.value!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)

        return TaskHolder(view, counter++)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = info.size

    inner class TaskHolder(itemView: View, id: Int) : RecyclerView.ViewHolder(itemView) {
        private var binding: TodoItemBinding = TodoItemBinding.bind(itemView)

        init {
            bind(id)
        }

        fun bind(i: Int) = with(binding) {
            val data = info[i]

            textViewTask.text = data.description

            checkBoxTask.isChecked = data.isCompleted
            checkCheckBox()
            checkBoxTask.setOnClickListener {
                checkCheckBox()
                changeIsCompleted(data)
                println("\nrep     = ${ListFragment.repository.tasks.value?.toList()}\nadapter = ${info}\n")
            }

            val clickListener = View.OnClickListener {
                itemView.findNavController().navigate(
                    R.id.action_listFragment_to_addFragment,
                    bundleOf(ListFragment.TASK_TAG to data)
                )
            }
            imageButtonTask.setOnClickListener(clickListener)
            textViewTask.setOnClickListener(clickListener)
        }

        private fun changeIsCompleted(item: TodoItem) {
            ListFragment.repository.updateItem(item.copy(isCompleted = item.isCompleted.not()))
        }

        private fun checkCheckBox() = with(binding) {
            if (checkBoxTask.isChecked) {
                textViewTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textViewTask.setTextColor(root.resources.getColor(R.color.label_tertiary, null))
            } else {
                textViewTask.paintFlags =
                    textViewTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textViewTask.setTextColor(root.resources.getColor(R.color.label_primary, null))
            }
        }
    }
}