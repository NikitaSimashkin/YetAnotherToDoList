package com.example.yetanothertodolist.ui.view.listFragment

import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.ui.model.TodoItem
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel

/**
 * Контроллер вьшек фрагмента листа
 */
class ListFragmentViewController(
    private val binding: ListFragmentBinding,
    private val viewModel: ListFragmentViewModel,
    private val adapter: TodoAdapter,
    private val lifecycleOwner: LifecycleOwner
) {
    private val context = binding.recyclerView.context

    companion object {
        const val TASK_TAG = "Task"
    }

    fun setUpView() {
        setUpAdapter()
        setFloatingButton()
        setUpNotificationButton()
    }

    private fun setUpNotificationButton() {
        binding.notification.isSelected = viewModel.noticeFlag ?: true
        binding.notification.setOnClickListener {
            val button = it as ImageButton
            button.isSelected = !button.isSelected
            viewModel.changeNoticeFlag()
        }
    }

    private fun setFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            binding.recyclerView.findNavController().navigate(
                R.id.action_listFragment_to_addFragment,
                bundleOf(TASK_TAG to null)
            )
        }
    }

    private fun setUpAdapter() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.tasks.observe(lifecycleOwner) {
            val callback =
                TodoAdapterDiffUtil(adapter.info, viewModel.tasks.value as List<TodoItem>)
            val res = DiffUtil.calculateDiff(callback)
            res.dispatchUpdatesTo(adapter)
            adapter.info = ArrayList(viewModel.tasks.value!!)
            updateNumber() // считаем колво выполненных заданий
        }
    }

    private fun updateNumber() {
        binding.completed.text =
            String.format(
                context.resources.getString(R.string.completed),
                viewModel.tasks.value!!.count { it.done }
            )
    }

}