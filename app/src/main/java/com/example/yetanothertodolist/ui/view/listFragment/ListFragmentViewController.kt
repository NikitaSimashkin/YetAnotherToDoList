package com.example.yetanothertodolist.ui.view.listFragment

import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.di.ListFragmentComponentViewScope
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import javax.inject.Inject

/**
 * Контроллер вьшек фрагмента листа
 */
@ListFragmentComponentViewScope
class ListFragmentViewController @Inject constructor(
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
        setEyeIcon()
    }

    private fun setEyeIcon() {
        changeEye()
        binding.eye.setOnClickListener {
            viewModel.eyeButton = !viewModel.eyeButton
            changeEye()
            updateAdapter()
        }
    }

    private fun changeEye() {
        if (viewModel.eyeButton)
            binding.eye.setImageResource(R.drawable.eye_icon)
        else
            binding.eye.setImageResource(R.drawable.eye_no_icon)
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
            updateAdapter()
            updateNumber()
        }
    }

    private fun updateAdapter(){
        val listToAdapter = if (viewModel.eyeButton) viewModel.tasks.value else viewModel.tasks.value!!.filter { !it.done }
        val callback = TodoAdapterDiffUtil(adapter.info, listToAdapter!!)
        val res = DiffUtil.calculateDiff(callback)
        res.dispatchUpdatesTo(adapter)
        adapter.info = ArrayList(listToAdapter)
    }

    private fun updateNumber() {
        binding.completed.text =
            String.format(
                context.resources.getString(R.string.completed),
                viewModel.tasks.value!!.count { it.done }
            )
    }

}