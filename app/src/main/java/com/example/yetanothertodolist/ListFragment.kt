package com.example.yetanothertodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.Adapters.Importance
import com.example.yetanothertodolist.Adapters.TaskAdapter
import com.example.yetanothertodolist.Adapters.TodoItem
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import java.time.LocalDateTime

class ListFragment: Fragment(R.layout.list_fragment) {
    private lateinit var binding: ListFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = ListFragmentBinding.bind(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = TaskAdapter(TodoItemRepository.list)

        binding.completed.text = String.format(resources.getString(R.string.completed), TodoItemRepository.list.count { it.isCompleted })

        binding.floatingActionButton.setOnClickListener {
            openAddFragment()
        }


    }

    private fun openAddFragment(){
        findNavController().navigate(R.id.action_listFragment_to_addFragment)
    }


}