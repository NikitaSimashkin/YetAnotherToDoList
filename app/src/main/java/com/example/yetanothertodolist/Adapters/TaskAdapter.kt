package com.example.yetanothertodolist.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TaskItemBinding
import java.time.LocalDateTime

enum class Importance{
    Low, Basic, Important
}

data class TodoItem(
    val id: String,
    val description: String,
    val importance: Importance,
    val isCompleted: Boolean,
    val dateOfCreation: LocalDateTime,
    val deadline: LocalDateTime? = null,
    val dateOfChange: LocalDateTime? = null
)

class TaskAdapter(private var info: List<TodoItem>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {
    var counter = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val a: LocalDateTime

        return TaskHolder(view, counter++)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = info.size

    inner class TaskHolder(private val itemView: View, private val id: Int) : RecyclerView.ViewHolder(itemView){
        private var binding: TaskItemBinding = TaskItemBinding.bind(itemView)

        init{
            bind(id)
        }

        fun bind(i: Int) = with(binding){
            val data = info[i]

            textViewTask.text = data.description
            imageButtonTask.setOnClickListener { println(1) }

            checkBoxTask.isChecked = data.isCompleted
            checkCheckBox()
            checkBoxTask.setOnClickListener {
                checkCheckBox()
            }
        }

        private fun checkCheckBox() = with(binding){
            if (checkBoxTask.isChecked){
                textViewTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textViewTask.setTextColor(root.resources.getColor(R.color.label_tertiary, null))
            } else {
                textViewTask.paintFlags = textViewTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textViewTask.setTextColor(root.resources.getColor(R.color.label_primary, null))
            }
        }
    }
}