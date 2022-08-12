package com.example.yetanothertodolist.ui.view.listFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.ioc.listFragment.ListFragmentComponent
import com.example.yetanothertodolist.ioc.listFragment.ListFragmentViewComponent
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel

/**
 * Фрагмент, на котором отображается список заданий
 */
class ListFragment : Fragment(R.layout.list_fragment) {

    private val viewModel: ListFragmentViewModel by activityViewModels {
        (requireContext().applicationContext as YetAnotherApplication).applicationComponent.viewModelFactory
    }

    private lateinit var listFragmentComponent: ListFragmentComponent

    private var listFragmentViewComponent: ListFragmentViewComponent? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = ListFragmentBinding.bind(view)
        listFragmentViewComponent =
            ListFragmentViewComponent(binding, viewModel, listFragmentComponent, this)
        listFragmentViewComponent!!.listFragmentViewController.setUpView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFragmentComponent = ListFragmentComponent(
            requireContext(),
            viewModel,
            this,
            (requireContext().applicationContext as YetAnotherApplication).applicationComponent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listFragmentViewComponent = null
    }
}