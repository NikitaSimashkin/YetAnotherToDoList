package com.example.yetanothertodolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.Adapters.Importance
import com.example.yetanothertodolist.Adapters.ImportanceAdapter
import com.example.yetanothertodolist.Adapters.TodoItem
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import java.time.LocalDate
import java.time.LocalDateTime

class AddFragment : Fragment(R.layout.add_fragment) {

    private lateinit var binding: AddFragmentBinding
    private var datePicker: DatePickerDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = AddFragmentBinding.bind(view)

        binding.close.setOnClickListener { closeFragment() }

        val arrayAdapter =
            context?.let { ImportanceAdapter(it, R.layout.importance_item, R.id.spinner_item) }
        binding.spinner.adapter = arrayAdapter

        // настройка работы выбора/смены даты
        binding.calendarSwitch.isClickable = false
        binding.setDateClickListener.setOnClickListener {
            if (binding.date.text.isEmpty()) {
                showDatePicker(view)
            } else {
                binding.date.text = ""
            }
            switch()
        }
        binding.changeDateClickListener.setOnClickListener {
            if (binding.date.text.isNotEmpty())
                showDatePicker(view)
        }

        val task: Any? = requireArguments().get(ListFragment.TASK_TAG) // редактируемое задание
        if (task != null) {
            setTask(task as TodoItem)
            binding.delete.setOnClickListener {
                removeTask(task)
                closeFragment()
            }
        } else {
            val color =
                view.context.getColor(R.color.label_disable) // делаем кнопку удалить некликабельной, если задания нет
            binding.deleteText.setTextColor(color)
            binding.deleteIcon.setColorFilter(color)
            binding.spinner.setSelection(Importance.Basic.ordinal)
        }

        binding.save.setOnClickListener {
            val date = binding.date.text.toString()

            when (task) {
                null -> {
                    if (addTask())
                        closeFragment()
                }
                else -> {
                    val t = (task as TodoItem)
                    t.description = binding.description.text.toString()
                    t.importance = Importance.getImportance(binding.spinner.selectedItemId.toInt())
                    t.dateOfChange = LocalDateTime.now()
                    t.deadline = if (date.isNotEmpty()) LocalDate.of(
                        date.substring(0, 4).toInt(),
                        date.substring(5, 7).toInt(),
                        date.substring(8).toInt()
                    ) else null

                    closeFragment()
                }
            }
        }

    }

    private fun setTask(task: TodoItem) = with(binding) {
        binding.description.setText(task.description)
        binding.spinner.setSelection(task.importance.ordinal)

        if (task.deadline != null) {
            binding.date.text = task.deadline.toString()
            binding.calendarSwitch.isChecked = true
        }
    }


    private fun showDatePicker(view: View) {
        if (datePicker == null) {
            datePicker = DatePickerDialog(view.context, R.style.Calendar)
            datePicker!!.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                view.resources.getText(R.string.ready),
                datePicker
            )
            datePicker!!.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                view.resources.getText(R.string.cancel),
                datePicker
            )

            datePicker!!.setOnDateSetListener { _, year, month, dayOfMonth ->
                setDate(year, month, dayOfMonth)
            }
            datePicker!!.setOnCancelListener {
                if (binding.date.text.isEmpty())
                    binding.calendarSwitch.isChecked = false
            }
        }
        datePicker!!.show()
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDate.of(year, month, dayOfMonth)
        binding.date.text = date.toString()
    }

    private fun switch() {
        binding.calendarSwitch.isChecked = !binding.calendarSwitch.isChecked
    }

    private fun addTask(): Boolean {
        if (binding.description.text.isNotEmpty()) {
            val date = binding.date.text.toString()

            ListFragment.repository.addItem(
                TodoItem
                    (
                    (Math.random() * 100 + 1).toString(),
                    binding.description.text.toString(),
                    Importance.getImportance(binding.spinner.selectedItemId.toInt()),
                    false,
                    LocalDateTime.now(),
                    if (date.isNotEmpty()) LocalDate.of(
                        date.substring(0, 4).toInt(),
                        date.substring(5, 7).toInt(),
                        date.substring(8).toInt()
                    ) else null
                )
            )

            return true
        }
        return false
    }

    private fun removeTask(item: TodoItem) {
        ListFragment.repository.removeItem(item)
    }

    private fun closeFragment() {
        findNavController().popBackStack()
    }

}