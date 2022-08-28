package com.example.yetanothertodolist.ui.view.listFragment

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.animations.BoxOfAnimations
import com.example.yetanothertodolist.databinding.ListFragmentBinding
import com.example.yetanothertodolist.di.ListFragmentComponentViewScope
import com.example.yetanothertodolist.other.getColor
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import com.example.yetanothertodolist.ui.view.listFragment.recycler.SwipeController
import com.example.yetanothertodolist.ui.view.listFragment.recycler.TodoAdapter
import com.example.yetanothertodolist.ui.view.listFragment.recycler.TodoAdapterDiffUtil
import com.google.android.material.appbar.AppBarLayout
import javax.inject.Inject

/**
 * Контроллер вьшек фрагмента листа
 */
@ListFragmentComponentViewScope
class ListFragmentViewController @Inject constructor(
    private val binding: ListFragmentBinding,
    private val viewModel: ListFragmentViewModel,
    private val adapter: TodoAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val listFragmentOpenCloseController: ListFragmentOpenCloseController,
    private val themeSelector: ThemeSelector,
    private val swipeController: SwipeController
) {
    private val context = binding.recyclerView.context

    fun setUpView() {
        setUpAdapter()
        setFloatingButton()
        setEyeButton()
        setSettingsButton()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        ItemTouchHelper(swipeController.getSwipeCallback()).attachToRecyclerView(binding.recyclerView)
    }

    private fun setSettingsButton() {
        binding.settingsLayout.setOnClickListener {
            themeSelector.showDialog()
        }
    }

    fun setUpScrolls() {
        binding.scroll.scrollY = viewModel.scrollPosition

        val behavior =
            ((binding.appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior)
        behavior.topAndBottomOffset = viewModel.appBarOffset
        binding.appBarLayout.requestLayout()
    }

    private fun setEyeButton() {
        viewModel.eyeButton.value?.let { changeIconEye(it) }
        binding.eyeLayout.setOnClickListener {
            viewModel.changeEye()
        }
        viewModel.eyeButton.observe(lifecycleOwner) {
            changeIconEye(it)
            updateAdapter()
        }
    }

    private fun changeIconEye(isOpen: Boolean) {
        if (isOpen)
            binding.eye.setImageResource(R.drawable.eye_icon)
        else
            binding.eye.setImageResource(R.drawable.eye_no_icon)
    }


    /**
     * Не понял зачем делать анимацию кнопки только если список пустой, поэтому я сделал
     * постоянную анимацию
     */
    private fun setFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            BoxOfAnimations.changeSizeAndColorAnimation(
                it,
                getColor(context, R.attr.color_blue),
                getColor(context, R.attr.color_red)
            )
            listFragmentOpenCloseController.plusButtonClose()
        }
    }

    private fun setUpAdapter() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.tasks.observe(lifecycleOwner) {
            updateAdapter()
            updateNumber()
        }
    }

    private fun updateAdapter() {
        val listToAdapter = viewModel.getListToAdapter()
        val callback = TodoAdapterDiffUtil(adapter.info, listToAdapter)
        val res = DiffUtil.calculateDiff(callback)
        res.dispatchUpdatesTo(adapter)
        adapter.info = ArrayList(listToAdapter)
    }

    private fun updateNumber() {
        binding.completed.text =
            String.format(
                context.resources.getString(R.string.completed),
                viewModel.getDoneTasks
            )
    }

}