package com.example.yetanothertodolist.ui.view.listFragment.recycler

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.yetanothertodolist.R
import com.example.yetanothertodolist.di.ListFragmentComponentScope
import com.example.yetanothertodolist.other.getColor
import com.example.yetanothertodolist.ui.stateholders.ListFragmentViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

@ListFragmentComponentScope
class SwipeController @Inject constructor(
    private val viewModel: ListFragmentViewModel,
    private val context: Context
) {

    fun getSwipeCallback() =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            private val limit = dpToPx(100, context)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = (viewHolder as TaskHolder).getItem()
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteItem(item)
                    }
                    ItemTouchHelper.RIGHT -> {
                        viewModel.changeCheckBox(item, true)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                ).addSwipeLeftActionIcon(R.drawable.icon_delete)
                    .addSwipeLeftBackgroundColor(getColor(context, R.attr.color_red))
                    .addSwipeRightActionIcon(R.drawable.done_icon)
                    .addSwipeRightBackgroundColor(getColor(context, R.attr.color_green))
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

    private fun dpToPx(value: Int, context: Context): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }
}