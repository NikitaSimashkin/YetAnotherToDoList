package com.example.yetanothertodolist.ui.view.listFragment

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.example.yetanothertodolist.ui.stateholders.Action
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.time.LocalDateTime

/**
 * ViewHolder для TodoAdapter, отображает одно задание
 */
class TaskHolder(itemView: View, private val viewModel: ListFragmentViewModel) :
    RecyclerView.ViewHolder(itemView) {
    private var binding: TodoItemBinding = TodoItemBinding.bind(itemView)

    fun bind(itemCopy: TodoItem) = with(binding) {

        setDescription(itemCopy)

        setCheckBox(itemCopy)

        setClickListenersToOpenAddFragment(itemCopy)

        setImportance(itemCopy)

        setDate(itemCopy)
    }

    private fun setDate(data: TodoItem) {
        binding.dateText.text =
            if (data.deadline != null) data.deadline.toLocalDate().toString() else ""
    }

    private fun setImportance(data: TodoItem) {
        when (data.importance) {
            Importance.Low -> {
                binding.checkBoxTask.buttonTintList =
                    ColorStateList(TodoAdapter.states, TodoAdapter.colorsForLowAndBasic)
                binding.iconImportance.setBackgroundResource(R.drawable.low_icon)
            }
            Importance.Basic -> {
                binding.checkBoxTask.buttonTintList =
                    ColorStateList(TodoAdapter.states, TodoAdapter.colorsForLowAndBasic)
                binding.iconImportance.background = null
            }
            Importance.Important -> {
                binding.checkBoxTask.buttonTintList =
                    ColorStateList(TodoAdapter.states, TodoAdapter.colorsForImportant)
                binding.iconImportance.setBackgroundResource(R.drawable.important_icon)
            }
        }
    }

    private fun setCheckBox(data: TodoItem) = with(binding) {
        checkBoxTask.isChecked = data.done
        changeTextStyle()
        checkBoxTask.setOnClickListener {
            viewModel.callToRepository(
                Action.Update,
                data.copy(done = binding.checkBoxTask.isChecked, changedAt = LocalDateTime.now())
            )
            changeTextStyle()
        }
    }

    private fun setDescription(data: TodoItem) = with(binding) {
        textViewTask.text = data.description
    }

    private fun setClickListenersToOpenAddFragment(data: TodoItem) = with(binding) {
        val openAddFragment: View.OnClickListener = View.OnClickListener {
            itemView.findNavController().navigate(
                R.id.action_listFragment_to_addFragment,
                bundleOf(ListFragmentViewController.TASK_TAG to data)
            )
        }
        todoItemLayout.setOnClickListener(openAddFragment)
        iconlayout.setOnClickListener(openAddFragment)
        imagebuttontasklayout.setOnClickListener(openAddFragment)
        datelayout.setOnClickListener(openAddFragment)
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


/*
    onlyOneCoroutineScope, как следует из названия, может запускать только одну корутину для каждого задания
    Это сделано для того, чтобы стабилизировать поведение программы, когда пользователь включает
    режим тестировщика и меняет миллион раз чекбокс, оттого у нас получаются бесполезные корутины
//     */
//private fun updateRepository(item: TodoItem) {
//    MainActivity.scope.launch(Dispatchers.IO) {
//        if (adapter.map.containsKey(item.id) && !adapter.map[item.id]!!.isCancelled) {
//            adapter.map[item.id]!!.cancel()
//        }
//        adapter.map[item.id] =
//            launch(Dispatchers.IO) {
//                try {
//                    adapter.repository.updateItem(item.copy(done = binding.checkBoxTask.isChecked))
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        when (e) {
//                            is FourZeroZeroException -> {
//                                adapter.snackbar.setText(AddFragment.revisionError)
//                            }
//                            is FourZeroFourException -> {
//                                adapter.snackbar.setText(AddFragment.elementNotFount)
//                            }
//                            is FourZeroOneException, is FiveZeroZeroException -> {
//                                adapter.snackbar.setText(AddFragment.unknownError)
//                            }
//                            is UnknownHostException -> {
//                                adapter.snackbar.setText(AddFragment.noInternet)
//                            }
//                        }
//
//                        adapter.snackbar.setAction(AddFragment.retry) {
//                            MainActivity.scope.launch(Dispatchers.IO) {
//                                try {
//                                    adapter.repository.serverUpdate(item.copy(done = binding.checkBoxTask.isChecked))
//                                } catch (e: Exception) {
//                                    Snackbar.make(
//                                        adapter.containerForSnackBar,
//                                        AddFragment.later, Snackbar.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        }.show()
//                    }
//                }
//            }
//
//
//    }
//}