package com.example.yetanothertodolist.Fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.Adapters.TodoAdapter
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoAdapterDiffUtil
import com.example.yetanothertodolist.Adapters.TodoAdapterClasses.TodoItem
import com.example.yetanothertodolist.MainActivity
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.TodoItemRepository
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.list_fragment) {
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: TodoAdapter

    companion object {
        val TASK_TAG = "Task"
        private lateinit var repository: TodoItemRepository // странное место для хранения репозитория, зачем и тут и в application?

        fun addList(item: TodoItem) = MainActivity.scope.launch {
                repository.addServerElement(item) // я понимаю, что не используется, но тут был бы поход в сеть на основном потоке
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

    private fun updateNumber() { // хорошо бы сделать лайвдатой
        binding.completed.text =
            String.format(resources.getString(R.string.completed), repository.numberOfCompleted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = (activity?.application as YetAnotherApplication).repository // вроде не гарантируется, что в этом месте у фрагмента есть activity, потенциальный креш
        adapter = TodoAdapter(repository, requireActivity().applicationContext)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
