package com.example.yetanothertodolist.ui.view.listFragment

import android.transition.TransitionInflater
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.databinding.TodoItemBinding
import com.example.yetanothertodolist.di.ListFragmentComponentScope
import com.example.yetanothertodolist.other.ConstValues
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.google.android.material.appbar.AppBarLayout
import javax.inject.Inject

@ListFragmentComponentScope
class ListFragmentOpenCloseController @Inject constructor(
    val fragment: ListFragment,
    val viewModel: ListFragmentViewModel
) {

    fun plusButtonClose() {
        TransitionInflater.from(fragment.context).inflateTransition(R.transition.fade)
            .also { fragment.exitTransition = it }

        saveScrollPosition()

        fragment.findNavController().navigate(
            R.id.action_listFragment_to_addFragment,
            bundleOf(ConstValues.TASK_TAG to null)
        )
    }

    fun saveScrollPosition() {
        val view = fragment.requireView()
        viewModel.scrollPosition = view.findViewById<NestedScrollView>(R.id.scroll).scrollY
        viewModel.appBarOffset =
            ((view.findViewById<AppBarLayout>(R.id.appBarLayout).layoutParams as CoordinatorLayout.LayoutParams).
            behavior as AppBarLayout.Behavior).topAndBottomOffset
    }

    fun taskHolderClose(binding: TodoItemBinding, data: TodoItem) = with(binding) {
        TransitionInflater.from(fragment.context).inflateTransition(R.transition.fade)
            .also { fragment.exitTransition = it }

        saveScrollPosition()

        val extras = FragmentNavigatorExtras(
            textViewTask to textViewTask.transitionName
        )
        textViewTask.findNavController().navigate(
            R.id.action_listFragment_to_addFragment,
            bundleOf(
                ConstValues.TASK_TAG to data,
                ConstValues.ID_TAG to textViewTask.transitionName
            ),
            null,
            extras
        )
    }

    fun startAnimation() {
        val animation =
            TransitionInflater.from(fragment.context).inflateTransition(R.transition.move)
        fragment.sharedElementEnterTransition = animation
        //fragment.enterTransition =
        //    TransitionInflater.from(fragment.context).inflateTransition(R.transition.fade)
    }
}