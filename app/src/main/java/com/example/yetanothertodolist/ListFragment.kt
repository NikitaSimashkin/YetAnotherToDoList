package com.example.yetanothertodolist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.Adapters.TodoAdapter
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoAdapterDiffUtil
import com.example.yetanothertodolist.databinding.ListFragmentBinding

class ListFragment : Fragment(R.layout.list_fragment) {
    private lateinit var binding: ListFragmentBinding
    private val adapter: TodoAdapter = TodoAdapter()

    companion object {
        val TASK_TAG = "Task"

        val repository = TodoItemRepository()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ListFragmentBinding.bind(view)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        repository.tasks.observe(viewLifecycleOwner) {
           // println("\nrep =     ${repository.tasks.value?.toList()}\nadapter = ${adapter.info}\n")
            val callback = TodoAdapterDiffUtil(adapter.info, repository.tasks.value!!)
            val res = DiffUtil.calculateDiff(callback)
            res.dispatchUpdatesTo(adapter)
            adapter.info = ArrayList(repository.tasks.value!!)
            updateNumber()
          //  println("\nrep =     ${repository.tasks.value?.toList()}\nadapter = ${adapter.info}\n")
        }
        binding.recyclerView.adapter = adapter

        updateNumber() // считаем колво выполненных заданий

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_listFragment_to_addFragment,
                bundleOf(ListFragment.TASK_TAG to null)
            )
        }
    }

    private fun updateNumber() {
        binding.completed.text =
            String.format(resources.getString(R.string.completed), repository.numberOfCompleted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}