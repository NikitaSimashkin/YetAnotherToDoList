package com.example.yetanothertodolist.Fragments

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.Adapters.TodoAdapter
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.Importance
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoAdapterDiffUtil
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.Backend.Converter
import com.example.yetanothertodolist.Backend.ServerList
import com.example.yetanothertodolist.Backend.ServerOneElement
import com.example.yetanothertodolist.MainActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.TodoItemRepository
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import java.lang.RuntimeException
import java.time.LocalDate
import kotlin.random.Random

class ListFragment : Fragment(R.layout.list_fragment) {
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: TodoAdapter

    companion object {
        val TASK_TAG = "Task"
        private lateinit var repository: TodoItemRepository

        fun addList(item: TodoItem) = MainActivity.scope.launch {
                repository.addServerElement(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ListFragmentBinding.bind(view)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        repository.tasks.observe(viewLifecycleOwner) {
            val callback = TodoAdapterDiffUtil(adapter.info, repository.tasks.value!!)
            val res = DiffUtil.calculateDiff(callback)
            res.dispatchUpdatesTo(adapter)
            adapter.info = ArrayList(repository.tasks.value!!)
            updateNumber()
        }

        adapter.containerForSnackBar = requireActivity().findViewById(R.id.main_root)
        adapter.snackbar = Snackbar.make(adapter.containerForSnackBar, AddFragment.unknownError, Snackbar.LENGTH_LONG)
        binding.recyclerView.adapter = adapter

        updateNumber() // считаем колво выполненных заданий

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_listFragment_to_addFragment,
                bundleOf(TASK_TAG to null)
            )
        }
    }

    private fun updateNumber() {
        binding.completed.text =
            String.format(resources.getString(R.string.completed), repository.numberOfCompleted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = (activity?.application as YetAnotherApplication).repository
        adapter = TodoAdapter(repository, requireActivity().applicationContext)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}