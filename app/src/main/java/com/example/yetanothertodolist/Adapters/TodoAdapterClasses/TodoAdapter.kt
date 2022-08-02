package com.example.yetanothertodolist.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.ListFragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TodoItemBinding


class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TaskHolder>() {

    var info: List<TodoItem> = listOf()
    // var info: List<TodoItem> = ListFragment.repository.tasks.value!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(info[position])
    }

    override fun getItemCount(): Int = info.size


    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: TodoItemBinding = TodoItemBinding.bind(itemView)

        private val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())

        private val colorsForImportant = intArrayOf(
            itemView.context.resources.getColor(R.color.color_green, null),
            itemView.context.resources.getColor(R.color.color_red, null)
        )
        private val colorsForLowAndBasic = intArrayOf(
            itemView.context.resources.getColor(R.color.color_green, null),
            itemView.context.resources.getColor(R.color.color_gray, null)
        )

        fun bind(data: TodoItem) = with(binding) {
            textViewTask.text = data.description

            checkBoxTask.isChecked = data.isCompleted
            changeTextStyle()
            checkBoxTask.setOnClickListener {
                changeTextStyle()
                changeIsCompleted(data)
                // println("\nrep =     ${ListFragment.repository.tasks.value?.toList()}\nadapter = $info")
            }

            val openAddFragment: View.OnClickListener = View.OnClickListener{
                itemView.findNavController().navigate(
                    R.id.action_listFragment_to_addFragment,
                    bundleOf(ListFragment.TASK_TAG to data)
                )
            }
            todoItemLayout.setOnClickListener(openAddFragment)
            iconlayout.setOnClickListener(openAddFragment)
            imagebuttontasklayout.setOnClickListener(openAddFragment)
            datelayout.setOnClickListener(openAddFragment)

            when (data.importance) {
                Importance.Low -> {
                    binding.checkBoxTask.buttonTintList =
                        ColorStateList(states, colorsForLowAndBasic)
                    binding.iconImportance.setBackgroundResource(R.drawable.low_icon)
                }
                Importance.Basic -> {
                    binding.checkBoxTask.buttonTintList =
                        ColorStateList(states, colorsForLowAndBasic)
                    binding.iconImportance.background = null
                   // setPaddingLeft(0f)
                }
                Importance.Important -> {
                    binding.checkBoxTask.buttonTintList = ColorStateList(states, colorsForImportant)
                    binding.iconImportance.setBackgroundResource(R.drawable.important_icon)
                }
            }

            if (data.deadline != null){
                val lp = binding.datelayout.layoutParams
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                binding.dateText.text = data.deadline.toString()
            } else {
                val lp = binding.datelayout.layoutParams
                lp.height = 0
                binding.dateText.text = ""
            }
        }

        private fun changeIsCompleted(item: TodoItem) {
            ListFragment.repository.updateItem(item.copy(isCompleted = item.isCompleted.not()))
        }

        private fun changeTextStyle() = with(binding) {
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


fun dpFromPx(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun pxFromDp(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}