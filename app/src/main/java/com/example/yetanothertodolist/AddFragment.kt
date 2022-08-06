package com.example.yetanothertodolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.Adapters.ImportanceAdapter
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.MyInternetException
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.LocalDateTime

class AddFragment : Fragment(R.layout.add_fragment) {

    private lateinit var binding: AddFragmentBinding
    private lateinit var containerForSnackBar: View
    private var datePicker: DatePickerDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = AddFragmentBinding.bind(view)
        containerForSnackBar = requireActivity().findViewById(R.id.main_root)
        closeButtonSetUp()

        spinnerSetUp()

        calendarSetUp()

        val task: Any? = requireArguments().get(ListFragment.TASK_TAG) // редактируемое задание
        deleteButtonSetUp(task)
        saveButtonSetUp(view, task)

        setSavedDate(savedInstanceState)
    }

    private fun setSavedDate(savedInstanceState: Bundle?) {
        if (savedInstanceState?.getString("Date") != null) {
            binding.dateAddFragment.text = savedInstanceState.getString("Date")
        }
    }

    private fun saveButtonSetUp(view: View, task: Any?) {
        binding.save.setOnClickListener {
            binding.save.isClickable = false
            if (binding.description.text.isEmpty()) {
                binding.save.isClickable = true
                Toast.makeText(view.context, R.string.save_error, Toast.LENGTH_SHORT).show()
            } else {
                when (task) {
                    null -> {
                        addTask()
                    }
                    else -> {
                        val newTask = createNewTask(task as TodoItem)
                        updateTask(newTask)
                    }
                }
            }
        }
    }

    private fun deleteButtonSetUp(task: Any?) {
        if (task != null) {
            setTask(task as TodoItem)
            binding.delete.setOnClickListener {
                binding.delete.isClickable = false
                removeTask(task)
            }
        } else {
            val color =
                requireContext().getColor(R.color.label_disable) // делаем кнопку удалить некликабельной, если задания нет
            binding.deleteText.setTextColor(color)
            binding.deleteIcon.setColorFilter(color)
            binding.spinner.setSelection(Importance.Basic.ordinal)
        }
    }

    private fun calendarSetUp() {
        binding.calendarSwitch.isClickable = false
        binding.setDateClickListener.setOnClickListener {
            selectDate()
        }

        binding.changeDateClickListener.setOnClickListener {
            if (binding.dateAddFragment.text.isNotEmpty())
                showDatePicker()
            else {
                selectDate()
            }
        }
    }

    private fun spinnerSetUp() {
        val arrayAdapter =
            context?.let { ImportanceAdapter(it, R.layout.importance_item, R.id.spinner_item) }
        binding.spinner.adapter = arrayAdapter
        binding.spinnerLayout.setOnClickListener {
            binding.spinner.performClick()
        }
    }

    private fun closeButtonSetUp() {
        binding.close.setOnClickListener { closeFragment() }
    }

    private fun updateTask(newTask: TodoItem) {
        MainActivity.scope.launch(Dispatchers.IO) {
            try {
                ListFragment.repository.mutex.withLock {
                    ListFragment.repository.updateItem(newTask)
                }
            } catch (e: MyInternetException) {
                Snackbar.make(
                    containerForSnackBar,
                    containerForSnackBar.context.getText(R.string.internet_error),
                    Snackbar.LENGTH_SHORT
                ).setAction(
                    "OK"
                ) {}.show()
            }
        }
        closeFragment()
    }

    private fun createNewTask(oldTask: TodoItem): TodoItem {
        val date = binding.dateAddFragment.text.toString()
        return TodoItem(
            description = binding.description.text.toString(),
            importance = Importance.getImportance(binding.spinner.selectedItemId.toInt()),
            dateOfChange = LocalDateTime.now(),
            deadline = if (date.isNotEmpty()) LocalDate.of(
                date.substring(0, 4).toInt(),
                date.substring(5, 7).toInt(),
                date.substring(8).toInt()
            ) else null,
            id = oldTask.id,
            isCompleted = oldTask.isCompleted,
            dateOfCreation = oldTask.dateOfCreation
        )
    }

    private fun setTask(task: TodoItem) = with(binding) {
        binding.description.setText(task.description)
        binding.spinner.setSelection(task.importance.ordinal)

        if (task.deadline != null) {
            binding.dateAddFragment.text = task.deadline.toString()
            binding.calendarSwitch.isChecked = true
        }
    }


    private fun showDatePicker() {
        if (datePicker == null) {
            datePicker = DatePickerDialog(requireContext(), R.style.Calendar)
            datePicker!!.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                requireContext().getText(R.string.ready),
                datePicker
            )
            datePicker!!.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                requireContext().resources.getText(R.string.cancel),
                datePicker
            )

            datePicker!!.setOnDateSetListener { _, year, month, dayOfMonth ->
                setDate(year, month, dayOfMonth)
            }
            datePicker!!.setOnCancelListener {
                if (binding.dateAddFragment.text.isEmpty())
                    binding.calendarSwitch.isChecked = false
            }
        }
        datePicker!!.show()
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDate.of(year, month, dayOfMonth)
        binding.dateAddFragment.text = date.toString()
    }

    private fun switch() {
        binding.calendarSwitch.isChecked = !binding.calendarSwitch.isChecked
    }

    private fun addTask() {
        val date = binding.dateAddFragment.text.toString()
        val newTask = TodoItem(
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

        MainActivity.scope.launch(Dispatchers.IO) {
            try {
                ListFragment.repository.mutex.withLock {
                    ListFragment.repository.addItem(newTask)
                }
            } catch (e: MyInternetException) {
                Snackbar.make(
                    containerForSnackBar,
                    containerForSnackBar.context.getText(R.string.internet_error),
                    Snackbar.LENGTH_SHORT
                ).setAction(
                    "OK"
                ) {}.show()
            }
        }
        closeFragment()
    }

    private fun removeTask(item: TodoItem) {
        MainActivity.scope.launch (Dispatchers.IO){
            try {
                ListFragment.repository.mutex.withLock {
                    ListFragment.repository.removeItem(item)
                }
            } catch (e: MyInternetException) {
                Snackbar.make(
                    containerForSnackBar,
                    containerForSnackBar.context.getText(R.string.internet_error),
                    Snackbar.LENGTH_SHORT
                ).setAction(
                    "OK"
                ) {}.show()
            }
        }
        closeFragment()
    }

    private fun closeFragment() {
        findNavController().popBackStack()
    }

    private fun selectDate() {
        if (binding.dateAddFragment.text.isEmpty()) {
            showDatePicker()
        } else {
            binding.dateAddFragment.text = ""
        }
        switch()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Date", binding.dateAddFragment.text.toString())

    }

}