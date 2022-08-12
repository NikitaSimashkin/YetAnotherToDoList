package com.example.yetanothertodolist.ui.view.addFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.YetAnotherApplication
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.ioc.addFragment.AddFragmentComponent
import com.example.yetanothertodolist.ioc.addFragment.AddFragmentViewComponent
import com.example.yetanothertodolist.ui.stateholders.AddFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentViewController

/**
 * Фрагмент, на котором добавляются или редактируются задания
 */
class AddFragment : Fragment(R.layout.add_fragment) {

    private val addModel: AddFragmentViewModel by activityViewModels {
        (requireContext().applicationContext as YetAnotherApplication).applicationComponent.viewModelFactory
    }

    private lateinit var addFragmentComponent: AddFragmentComponent

    private var addFragmentViewComponent: AddFragmentViewComponent? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AddFragmentBinding.bind(view)

        var task: Any? =
            requireArguments().get(ListFragmentViewController.TASK_TAG) // редактируемое задание

        addFragmentViewComponent =
            AddFragmentViewComponent(binding, addModel, addFragmentComponent)
        addFragmentViewComponent!!.addFragmentViewController.setUpViews(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        addFragmentComponent = AddFragmentComponent(
            requireContext(),
            this,
            (requireContext().applicationContext as YetAnotherApplication).applicationComponent
        )
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        addFragmentViewComponent = null
        super.onDestroyView()
    }
}