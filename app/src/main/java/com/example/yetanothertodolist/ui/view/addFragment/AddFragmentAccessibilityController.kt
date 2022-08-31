package com.example.yetanothertodolist.ui.view.addFragment

import android.app.DatePickerDialog
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.di.AddFragmentComponentScope
import javax.inject.Inject

@AddFragmentComponentScope
class AddFragmentAccessibilityController @Inject constructor() {

    fun setUpAccessibility(binding: AddFragmentBinding) {
        val close = getCloseButtonDelegate(binding)
        binding.close.accessibilityDelegate = close

        val save = getSaveButtonDelegate(binding)
        binding.save.accessibilityDelegate = save

        setSpinnerSettings(binding)

        setSwitchSettings(binding)

        val doBy = getDoByDelegate(binding)
        binding.changeDateClickListener.accessibilityDelegate = doBy

        val delete = getDeleteButtonDelegate(binding)
        binding.delete.isFocusable = true
        binding.delete.accessibilityDelegate = delete
    }

    private fun getDeleteButtonDelegate(binding: AddFragmentBinding) =
        object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = Button::class.java.name
                info?.contentDescription = binding.root.context.getText(R.string.delete)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.delete_task)
                    )
                )
                info?.isEnabled = binding.delete.isClickable
            }
        }

    private fun getDoByDelegate(binding: AddFragmentBinding) =
        object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = Button::class.java.name
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.set_or_change_deadline)
                    )
                )
                info?.contentDescription = binding.root.context.getText(R.string.doBy).toString() +
                        binding.dateAddFragment.text.ifEmpty { " " }
            }
        }

    private fun setSwitchSettings(binding: AddFragmentBinding){
        binding.calendarSwitch.contentDescription = binding.root.context.getText(R.string.set_deadline)
    }

    private fun setSpinnerSettings(binding: AddFragmentBinding){
        binding.spinnerLayout.contentDescription =
            binding.calendar.context.getText(R.string.importance)
                .toString() + Importance.getTranslatedName(
                Importance.getImportance(binding.spinner.selectedItemPosition),
                binding.root.context
            )
    }

    private fun getSaveButtonDelegate(binding: AddFragmentBinding) =
        object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.save_task)
                    )
                )
                info?.className = Button::class.java.name
            }
        }

    private fun getCloseButtonDelegate(binding: AddFragmentBinding) =
        object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.close_add_fragment)
                    )
                )
            }
        }

    fun setUpDatePickerAccessibility(datePicker: DatePickerDialog) {
        val cancel = datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        datePickerCancelButtonAccessibility(cancel)

        val ready = datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
        datePickerReadyButtonAccessibility(ready)
    }

    private fun datePickerReadyButtonAccessibility(
        ready: Button
    ) {
        ready.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        ready.context.getText(R.string.save_date)
                    )
                )

            }
        }
    }

    private fun datePickerCancelButtonAccessibility(cancel: Button) {
        cancel.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        cancel.context.getText(R.string.close_date_picker)
                    )
                )

            }
        }
    }

    fun setUpImportantItemAccessibility(view: View, position: Int) {
        if (position == 2)
            view.contentDescription = view.context.getText(R.string.important)
    }

}