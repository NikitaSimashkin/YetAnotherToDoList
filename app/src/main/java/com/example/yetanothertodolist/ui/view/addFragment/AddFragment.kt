package com.example.yetanothertodolist.ui.view.addFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.di.AddFragmentComponent
import com.example.yetanothertodolist.di.AddFragmentComponentView
import com.example.yetanothertodolist.other.ConstValues
import com.example.yetanothertodolist.ui.view.MainActivity.MainActivity

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

        val task: TodoItem? =
            requireArguments().get(ConstValues.TASK_TAG) as TodoItem? // редактируемое задание

        addFragmentComponentView = addFragmentComponent.addFragmentComponentView()
            .create(binding).apply {
                addFragmentViewController.setUpViews(task)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (view != null)
            addFragmentComponent.addFragmentOpenCloseController().startAnimation(requireArguments(), view)
        return view
    }

    override fun onDestroyView() {
        addFragmentComponentView = null
        super.onDestroyView()
    }
}