package com.example.yetanothertodolist.ui.view.listFragment

import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.example.yetanothertodolist.di.ListFragmentComponentScope
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import javax.inject.Inject

@ListFragmentComponentScope
class ListFragmentAccessibilityController @Inject constructor(
    private val viewModel: ListFragmentViewModel
) {

    fun setUpAccessibility(binding: ListFragmentBinding) {
        settingsButtonAccessibility(binding)
        eyeButtonAccessibility(binding)
        plusButtonAccessibility(binding)
    }

    private fun settingsButtonAccessibility(binding: ListFragmentBinding) {
        val settings: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = Button::class.java.name
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.open)
                    )
                )
            }
        }
        binding.settingsLayout.accessibilityDelegate = settings
    }

    private fun eyeButtonAccessibility(binding: ListFragmentBinding) {
        val eye: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = CheckBox::class.java.name
                info?.isCheckable = true
                host?.findViewTreeLifecycleOwner()?.let { it ->
                    viewModel.eyeButton.observe(it) { bool ->
                        info?.isChecked = bool
                    }
                }
            }
        }

        binding.eyeLayout.accessibilityDelegate = eye
    }

    private fun plusButtonAccessibility(binding: ListFragmentBinding) {
        val plus: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = Button::class.java.name
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        binding.root.context.getText(R.string.addTask)
                    )
                )
            }
        }

        binding.floatingActionButton.accessibilityDelegate = plus
    }

    fun setUpHolderAccessibility(itemBinding: TodoItemBinding, item: TodoItem) {
        val context = itemBinding.root.context
        setImportanceAccessibility(context, item, itemBinding)
        setDateAccessibility(item, context, itemBinding)
        setDescriptionAccessibility(itemBinding, item)
    }

    private fun setDescriptionAccessibility(itemBinding: TodoItemBinding, item: TodoItem) {
        val description: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        itemBinding.root.context.getText(R.string.edit)
                    )
                )
                info?.contentDescription = itemBinding.root.context.getText(R.string.description).toString() + item.description
            }
        }
        itemBinding.todoItemLayout.accessibilityDelegate = description
    }

    private fun setDateAccessibility(
        item: TodoItem,
        context: Context,
        itemBinding: TodoItemBinding
    ) {
        val date: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                if (item.deadline == null) {
                    host?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
                } else {
                    info?.contentDescription =
                        context.getText(R.string.doBy).toString() + itemBinding.dateText.text
                    info?.addAction(
                        AccessibilityNodeInfo.AccessibilityAction(
                            AccessibilityNodeInfo.ACTION_CLICK,
                            itemBinding.root.context.getText(R.string.edit)
                        )
                    )
                }

            }
        }
        itemBinding.dateText.accessibilityDelegate = date
    }

    private fun setImportanceAccessibility(
        context: Context,
        item: TodoItem,
        itemBinding: TodoItemBinding
    ) {
        val importance: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View?,
                info: AccessibilityNodeInfo?
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info?.className = TextView::class.java.name
                info?.contentDescription =
                    context.getText(R.string.importance).toString() + when (item.importance) {
                        Importance.Low -> {
                            context.getText(R.string.low)
                        }
                        Importance.Basic -> {
                            context.getText(R.string.basic)
                        }
                        Importance.Important -> {
                            context.getText(R.string.important)
                        }
                    }
                info?.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_CLICK,
                        itemBinding.root.context.getText(R.string.edit)
                    )
                )

            }
        }
        itemBinding.iconImportance.accessibilityDelegate = importance
    }
}