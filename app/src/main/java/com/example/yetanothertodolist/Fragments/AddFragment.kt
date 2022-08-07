package com.example.yetanothertodolist.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.Adapters.ImportanceAdapter
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.*
import com.example.yetanothertodolist.MainActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.TodoItemRepository
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withLock
import java.lang.NullPointerException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

enum class Action {
    Remove, Update, Add
}

class AddFragment : Fragment(R.layout.add_fragment) {

    private lateinit var binding: AddFragmentBinding
    private lateinit var containerForSnackBar: View
    private lateinit var repository: TodoItemRepository
    private var datePicker: DatePickerDialog? = null
    private lateinit var snackBar: Snackbar

    companion object {
        lateinit var retry: String
        lateinit var revisionError: String
        lateinit var elementNotFount: String
        lateinit var unknownError: String
        lateinit var later: String
        lateinit var noInternet: String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = AddFragmentBinding.bind(view)
        containerForSnackBar = requireActivity().findViewById(R.id.main_root)
        snackBar = Snackbar.make(
            containerForSnackBar,
            containerForSnackBar.context.getText(R.string.elementNotFoundError),
            Snackbar.LENGTH_LONG
        )
        repository = (requireActivity().application as YetAnotherApplication).repository


        closeButtonSetUp()

        spinnerSetUp()

        calendarSetUp()

        val task: Any? = requireArguments().get(ListFragment.TASK_TAG) // редактируемое задание
        if (task != null) {
            setTask(task as TodoItem)
        }
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
            binding.delete.setOnClickListener {
                binding.delete.isClickable = false
                removeTask(task as TodoItem)
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
        changeRepository(Action.Update, newTask)
        closeFragment()
    }

    private fun createNewTask(oldTask: TodoItem): TodoItem {
        val date = binding.dateAddFragment.text.toString()
        return TodoItem(
            description = binding.description.text.toString(),
            importance = Importance.getImportance(binding.spinner.selectedItemId.toInt()),
            changedAt = LocalDateTime.now(),
            deadline = if (date.isNotEmpty()) LocalDateTime.of(
                LocalDate.parse(date),
                LocalTime.of(0, 0, 0)
            ) else null,
            id = oldTask.id,
            done = oldTask.done,
            createdAt = oldTask.createdAt,
            lastUpdateBy = oldTask.lastUpdateBy
        )
    }

    private fun setTask(task: TodoItem) = with(binding) {
        binding.description.setText(task.description)
        binding.spinner.setSelection(task.importance.ordinal)

        if (task.deadline != null) {
            binding.dateAddFragment.text = task.deadline.toLocalDate().toString()
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
                resources.getText(R.string.cancel),
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
            id = UUID.randomUUID().toString(),
            description = binding.description.text.toString(),
            importance = Importance.getImportance(binding.spinner.selectedItemId.toInt()),
            done = false,
            createdAt = LocalDateTime.now(),
            deadline = if (date.isNotEmpty()) LocalDateTime.of(
                LocalDate.parse(date),
                LocalTime.of(0, 0, 0)
            ) else null,
            lastUpdateBy = "Me",
            changedAt = LocalDateTime.now()
        )

        changeRepository(Action.Add, newTask)

        closeFragment()
    }

    private fun removeTask(item: TodoItem) {
        changeRepository(Action.Remove, item)
        closeFragment()
    }

    // add/update/delete
    private fun changeRepository(
        action: Action,
        item: TodoItem
    ) {
        MainActivity.scope.launch(Dispatchers.IO) {
            try {
                when (action) {
                    Action.Add -> repository.addItem(item)
                    Action.Remove -> repository.removeItem(item)
                    Action.Update -> repository.updateItem(item)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    when (e) {
                        is FourZeroZeroException -> {
                            snackBar.setText(revisionError)
                        }
                        is FourZeroFourException -> {
                            snackBar.setText(elementNotFount)
                        }
                        is FourZeroOneException, is FiveZeroZeroException -> {
                            snackBar.setText(unknownError)
                        }
                        is UnknownHostException -> {
                            snackBar.setText(noInternet)
                        }
                    }

                    snackBar.setAction(retry) {
                        MainActivity.scope.launch(Dispatchers.IO) {
                            try {
                                when (action) {
                                    Action.Add -> repository.addServerElement(item)
                                    Action.Remove -> repository.deleteServerElement(item.id)
                                    Action.Update -> repository.updateServerElement(item)
                                }
                            } catch(e: Exception){
                                Snackbar.make(containerForSnackBar, later, Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }.show()
                }
            }
        }
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