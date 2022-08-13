package com.example.yetanothertodolist.ui.view.addFragment

import android.app.DatePickerDialog
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.ui.stateholders.*
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Контроллер вьюшек для addFragment */
class AddFragmentViewController(
    private val binding: AddFragmentBinding,
    private val addModel: AddFragmentViewModel,
    private val adapter: ImportanceAdapter
) {
    private var datePicker: DatePickerDialog? = null
    private val context = binding.close.context

    fun setUpViews(task: Any?) {
        if (!addModel.valuesAlreadySet) {
            if (task == null)
                addModel.setStartValues()
            else
                addModel.setItemValues(task as TodoItem)
            addModel.valuesAlreadySet = true
        }
        spinnerSetUp()
        calendarSetUp()
        deleteAndCloseButtonSetUp(task)
        saveButtonSetUp(task)
        setModelField()
        setListenerForModel()
    }

    private fun setListenerForModel() {
        setListenerForModelDescription()
        setListenerForModelSpinner()
        setListenerForModelDate()
    }

    private fun setListenerForModelDate() {
        binding.dateAddFragment.doOnTextChanged { text, start, before, count ->
            if (text != null && text.isNotEmpty())
                addModel.deadline = LocalDateTime.of(LocalDate.parse(text), LocalTime.of(0, 0, 0))
            else
                addModel.deadline = null
        }
    }

    private fun setListenerForModelSpinner() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addModel.importance = Importance.getImportance(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setListenerForModelDescription() {
        binding.description.doOnTextChanged { text, start, before, count ->
            addModel.description = text.toString()
        }
    }

    private fun setModelField() = with(binding) {
        description.setText(addModel.description)
        spinner.setSelection(addModel.importance.ordinal)
        if (addModel.deadline != null) {
            dateAddFragment.text = addModel.deadline!!.toLocalDate().toString()
            calendarSwitch.isChecked = true
        } else {
            dateAddFragment.text = ""
            calendarSwitch.isChecked = false
        }
    }

    private fun calendarSetUp() {
        binding.calendarSwitch.isClickable = false
        binding.setDateClickListener.setOnClickListener {
            switch()
        }
        binding.changeDateClickListener.setOnClickListener {
            if (binding.dateAddFragment.text.isNotEmpty())
                showDatePicker()
            else {
                switch()
            }
        }
    }

    private fun showDatePicker() {
        if (datePicker == null) {
            createDatePicker()
        }
        datePicker!!.show()
    }

    private fun createDatePicker() {
        datePicker = DatePickerDialog(context, R.style.Calendar)
        datePicker!!.setButton(
            DatePickerDialog.BUTTON_POSITIVE,
            context.getText(R.string.ready),
            datePicker
        )
        datePicker!!.setButton(
            DatePickerDialog.BUTTON_NEGATIVE,
            context.getText(R.string.cancel),
            datePicker
        )
        datePicker!!.setOnDateSetListener { _, year, month, dayOfMonth ->
            binding.dateAddFragment.text = LocalDate.of(year, month, dayOfMonth).toString()
        }
        datePicker!!.setOnCancelListener {
            if (binding.dateAddFragment.text.isEmpty()) binding.calendarSwitch.isChecked = false
        }
    }

    private fun switch() {
        if (binding.dateAddFragment.text.isEmpty()) {
            showDatePicker()
        } else {
            binding.dateAddFragment.text = ""
        }
        binding.calendarSwitch.isChecked = !binding.calendarSwitch.isChecked
    }

    private fun saveButtonSetUp(task: Any?) {
        binding.save.setOnClickListener {
            binding.save.isClickable = false
            if (binding.description.text.trim().isEmpty()) {
                binding.save.isClickable = true
                Snackbar.make(binding.root, R.string.save_error, Snackbar.LENGTH_SHORT).show()
            } else {
                when (task) {
                    null -> addTask()
                    else -> {
                        val newTask = addModel.getItem()
                        updateTask(newTask)
                    }
                }
            }
        }
    }

    private fun deleteAndCloseButtonSetUp(task: Any?) {
        if (task != null) {
            binding.delete.setOnClickListener {
                binding.delete.isClickable = false
                deleteTask(task as TodoItem)
            }
        } else {
            val color = context.getColor(R.color.label_disable)
            binding.deleteText.setTextColor(color)
            binding.deleteIcon.setColorFilter(color)
        }

        binding.close.setOnClickListener { closeFragment() }
    }

    private fun spinnerSetUp() {
        binding.spinner.adapter = adapter
        binding.spinnerLayout.setOnClickListener {
            binding.spinner.performClick()
        }
    }

    private fun updateTask(item: TodoItem) {
        addModel.callToRepository(Action.Update, item)
        closeFragment()
    }

    private fun deleteTask(item: TodoItem) {
        addModel.callToRepository(Action.Delete, item)
        closeFragment()
    }

    private fun addTask() {
        addModel.callToRepository(Action.Add, addModel.getItem(true))
        closeFragment()
    }

    private fun closeFragment() {
        addModel.valuesAlreadySet = false
        binding.close.findNavController().popBackStack()
    }
}