package com.example.yetanothertodolist.ui.view.addFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.di.AddFragmentComponent
import com.example.yetanothertodolist.di.AddFragmentComponentView
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity
import com.example.yetanothertodolist.ui.view.listFragment.ListFragmentViewController

/**
 * Фрагмент, на котором добавляются или редактируются задания
 */
class AddFragment : Fragment(R.layout.add_fragment) {

    private val mainActivityComponent by lazy { (requireActivity() as MainActivity).mainActivityComponent }

    private val addFragmentComponent: AddFragmentComponent by lazy {
        mainActivityComponent.addFragmentComponent().create(this)
    }

    private var addFragmentComponentView: AddFragmentComponentView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = AddFragmentBinding.bind(view)

        val task: Any? =
            requireArguments().get(ListFragmentViewController.TASK_TAG) // редактируемое задание
        addFragmentComponentView = addFragmentComponent.addFragmentComponentView().create(binding)
        addFragmentComponentView!!.addFragmentViewController.setUpViews(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        addFragmentComponentView = null
        super.onDestroyView()
    }
}