package com.example.yetanothertodolist.ui.view.addFragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.databinding.AddFragmentBinding
import com.example.yetanothertodolist.di.AddFragmentComponentScope
import com.example.yetanothertodolist.other.ConstValues
import javax.inject.Inject

@AddFragmentComponentScope
class AddFragmentOpenCloseController @Inject constructor(
    private val fragment: AddFragment
) {

    private var task: TodoItem? = null

    fun startAnimation(requireArguments: Bundle, view: View) {
        val idTag = requireArguments.getString(ConstValues.ID_TAG)
        task = requireArguments.get(ConstValues.TASK_TAG) as TodoItem?

        if (idTag != null) {
            view.findViewById<EditText>(R.id.description).transitionName =
                idTag
            val animation =
                TransitionInflater.from(fragment.context).inflateTransition(R.transition.move)
            fragment.sharedElementEnterTransition = animation
        } else {
            fragment.enterTransition =
                TransitionInflater.from(fragment.context).inflateTransition(R.transition.bottom)
        }
    }

    fun closeButton(binding: AddFragmentBinding) {
        if (task == null) {
            fragment.exitTransition =
                TransitionInflater.from(fragment.context).inflateTransition(R.transition.bottom)
            fragment.findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            saveButton(task!!, binding)
        }
    }

    fun saveButton(item: TodoItem, binding: AddFragmentBinding) = with (binding) {
        description.transitionName = item.id

        val extras = FragmentNavigatorExtras(
            description to description.transitionName
        )
        fragment.findNavController().navigate(
            R.id.action_addFragment_to_listFragment,
            bundleOf(
                ConstValues.ID_TAG to description.transitionName
            ),
            null,
            extras
        )

        task = null
    }

    fun deleteButton(binding: AddFragmentBinding) {
        task = null
        fragment.exitTransition =
            TransitionInflater.from(fragment.context).inflateTransition(R.transition.top)
        fragment.findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }
}