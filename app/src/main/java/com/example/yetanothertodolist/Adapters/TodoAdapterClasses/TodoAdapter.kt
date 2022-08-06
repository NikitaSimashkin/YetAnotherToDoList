package com.example.yetanothertodolist.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.MyInternetException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.ListFragment
import com.example.yetanothertodolist.MainActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext


class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TaskHolder>() {

    var info: List<TodoItem> = listOf()

    lateinit var containerForSnackBar: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(info[position])
    }

    override fun getItemCount(): Int = info.size


    private val map = HashMap<String, Job>() // для метода changeIsCompleted

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
            }

            val openAddFragment: View.OnClickListener = View.OnClickListener {
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

            if (data.deadline != null) {
                val lp = binding.datelayout.layoutParams
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                binding.dateText.text = data.deadline.toString()
            } else {
                val lp = binding.datelayout.layoutParams
                lp.height = 0
                binding.dateText.text = ""
            }
        }

        /*
        onlyOneCoroutineScope, как следует из названия, может запускать только одну корутину для каждого задания
        Это сделано для того, чтобы стабилизировать поведение программы, когда пользователь включает
        режим тестировщика и меняет миллион раз чекбокс, оттого у нас получаются бесполезные корутины
         */
        private fun changeIsCompleted(item: TodoItem) {
            MainActivity.scope.launch(Dispatchers.IO) {
                ListFragment.repository.mutex.withLock {
                    if (map.containsKey(item.id) && !map[item.id]!!.isCancelled) {
                        map[item.id]!!.cancel()
                    }
                    map[item.id] =
                        launch(Dispatchers.IO) {
                            try {
                                ListFragment.repository.updateItem(item.copy(isCompleted = binding.checkBoxTask.isChecked))
                            } catch (e: MyInternetException) {

                                Snackbar.make(
                                    containerForSnackBar,
                                    itemView.context.getText(R.string.internet_error),
                                    Snackbar.LENGTH_SHORT
                                ).setAction(
                                    "OK"
                                ) {}.show()
                            }
                        }

                }
            }
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