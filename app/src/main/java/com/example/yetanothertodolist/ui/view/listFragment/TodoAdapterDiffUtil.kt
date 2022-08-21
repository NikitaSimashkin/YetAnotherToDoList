package com.example.yetanothertodolist.ui.view.listFragment

import androidx.recyclerview.widget.DiffUtil
import com.example.yetanothertodolist.data.model.TodoItem

/**
 * Класс для обновления адаптера
 */
class TodoAdapterDiffUtil(private val oldList: List<TodoItem>, private val newList: List<TodoItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old == new
    }
}