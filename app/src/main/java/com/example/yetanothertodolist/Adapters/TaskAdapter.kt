package com.example.yetanothertodolist.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TaskItemBinding
import java.util.zip.Inflater

data class Task(
    val isCompleted: Boolean,
    val importance: Int,
    val data: String,
    val description: String
)

class TaskAdapter(private var info: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {
    var counter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)

        return TaskHolder(view, counter++)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = info.size

    inner class TaskHolder(private val itemView: View, private val id: Int) : RecyclerView.ViewHolder(itemView){
        private lateinit var binding: TaskItemBinding

        init{
            binding = TaskItemBinding.bind(itemView)
            bind(id)
        }

        fun bind(i: Int) = with(binding){
            val data = info[i]

            checkBoxTask.isChecked = data.isCompleted
            textViewTask.text = data.description
            imageButtonTask.setOnClickListener { println(1) }
        }
    }
}