package com.example.yetanothertodolist.ui.view.listFragment

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.example.yetanothertodolist.other.getColor
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.addFragment.Importance

/**
 * ViewHolder для TodoAdapter, отображает одно задание
 */
class TaskHolder(
    itemView: View,
    private val viewModel: ListFragmentViewModel,
    private val listFragmentOpenCloseController: ListFragmentOpenCloseController
) :
    RecyclerView.ViewHolder(itemView) {
    private var binding: TodoItemBinding = TodoItemBinding.bind(itemView)

    fun bind(itemCopy: TodoItem) = with(binding) {
        textViewTask.transitionName = itemCopy.id

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
            viewModel.changeCheckBox(data, checkBoxTask.isChecked)
            changeTextStyle()
        }
    }

    private fun setDescription(data: TodoItem) = with(binding) {
        textViewTask.text = data.description
    }

    private fun setClickListenersToOpenAddFragment(data: TodoItem) = with(binding) {
        val close: View.OnClickListener = View.OnClickListener {
            listFragmentOpenCloseController.taskHolderClose(binding, data)
        }
        todoItemLayout.setOnClickListener(close)
        iconlayout.setOnClickListener(close)
        imagebuttontasklayout.setOnClickListener(close)
        datelayout.setOnClickListener(close)
    }

    private fun changeTextStyle() = with(binding) {
        if (checkBoxTask.isChecked) {
            textViewTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            textViewTask.setTextColor(getColor(root.context, R.attr.label_tertiary))
        } else {
            textViewTask.paintFlags =
                textViewTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            textViewTask.setTextColor(getColor(root.context, R.attr.label_primary))
        }
    }
}