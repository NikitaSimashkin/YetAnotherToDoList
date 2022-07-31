package com.example.yetanothertodolist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.Adapters.TodoAdapter
import com.example.yetanothertodolist.databinding.ListFragmentBinding

interface CheckboxCallback{
    fun update()
}

class ListFragment: Fragment(R.layout.list_fragment), CheckboxCallback{
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: TodoAdapter

    companion object{
        val TASK_TAG = "Task"

        val repository = TodoItemRepository()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ListFragmentBinding.bind(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        adapter.counter = 0
        binding.recyclerView.adapter = adapter
        binding.flRecyclerView.clipChildren = false

        update() // считаем колво выполненных заданий

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment, bundleOf(ListFragment.TASK_TAG to null))
        }
    }

    override fun update() {
        binding.completed.text = String.format(resources.getString(R.string.completed), repository.numberOfCompleted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TodoAdapter(this)
    }
}