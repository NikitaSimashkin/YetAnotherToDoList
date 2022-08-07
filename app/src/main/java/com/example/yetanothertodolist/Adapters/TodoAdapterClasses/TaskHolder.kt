package com.example.yetanothertodolist.Adapters.TodoAdapterClasses

import android.content.ContentValues.TAG
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.Adapters.TodoAdapter
import com.example.yetanothertodolist.Fragments.Action
import com.example.yetanothertodolist.Fragments.AddFragment
import com.example.yetanothertodolist.Fragments.ListFragment
import com.example.yetanothertodolist.MainActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.time.LocalDateTime

class TaskHolder(itemView: View, val adapter: TodoAdapter) : RecyclerView.ViewHolder(itemView) {
    private var binding: TodoItemBinding = TodoItemBinding.bind(itemView)

    fun bind(data: TodoItem) = with(binding) {
        textViewTask.text = data.description

        checkBoxTask.isChecked = data.done
        changeTextStyle()
        checkBoxTask.setOnClickListener {
            changeTextStyle()
            updateRepository(data)
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
                    ColorStateList(adapter.states, adapter.colorsForLowAndBasic)
                binding.iconImportance.setBackgroundResource(R.drawable.low_icon)
            }
            Importance.Basic -> {
                binding.checkBoxTask.buttonTintList =
                    ColorStateList(adapter.states, adapter.colorsForLowAndBasic)
                binding.iconImportance.background = null
                // setPaddingLeft(0f)
            }
            Importance.Important -> {
                binding.checkBoxTask.buttonTintList =
                    ColorStateList(adapter.states, adapter.colorsForImportant)
                binding.iconImportance.setBackgroundResource(R.drawable.important_icon)
            }
        }

        binding.dateText.text =
            if (data.deadline != null) data.deadline.toLocalDate().toString() else ""
//        if (data.deadline != null) {
//            val lp = binding.datelayout.layoutParams
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            binding.dateText.text = data.deadline.toLocalDate().toString()
//        } else {
//            val lp = binding.datelayout.layoutParams
//            lp.height = 0
//            binding.dateText.text = ""
//        }
    }

    /*
    onlyOneCoroutineScope, как следует из названия, может запускать только одну корутину для каждого задания
    Это сделано для того, чтобы стабилизировать поведение программы, когда пользователь включает
    режим тестировщика и меняет миллион раз чекбокс, оттого у нас получаются бесполезные корутины
     */
    private fun updateRepository(item: TodoItem) {
        MainActivity.scope.launch(Dispatchers.IO) {
            adapter.repository.mutex.withLock {
                if (adapter.map.containsKey(item.id) && !adapter.map[item.id]!!.isCancelled) {
                    adapter.map[item.id]!!.cancel()
                }
                adapter.map[item.id] =
                    launch(Dispatchers.IO) {
                        try {
                            adapter.repository.updateItem(item.copy(done = binding.checkBoxTask.isChecked))
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                when (e) {
                                    is FourZeroZeroException -> {
                                        adapter.snackbar.setText(AddFragment.revisionError)
                                    }
                                    is FourZeroFourException -> {
                                        adapter.snackbar.setText(AddFragment.elementNotFount)
                                    }
                                    is FourZeroOneException, is FiveZeroZeroException -> {
                                        adapter.snackbar.setText(AddFragment.unknownError)
                                    }
                                    is UnknownHostException -> {
                                        adapter.snackbar.setText(AddFragment.noInternet)
                                    }
                                }

                                adapter.snackbar.setAction(AddFragment.retry) {
                                    MainActivity.scope.launch(Dispatchers.IO) {
                                        try {
                                            adapter.repository.updateServerElement(item.copy(done = binding.checkBoxTask.isChecked))
                                        } catch (e: Exception) {
                                            Snackbar.make(
                                                adapter.containerForSnackBar,
                                                AddFragment.later, Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }.show()
                            }
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