package com.example.yetanothertodolist.ui.view.addFragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.yetanothertodolist.R

/**
 * Адаптер для spinner на фрагменте добавления
 */
class ImportanceAdapter(context: Context, resource: Int, textViewResourceId: Int) :
    ArrayAdapter<String>(context, resource, textViewResourceId) {

    init {
        addAll(context.resources.getStringArray(R.array.importance).toList())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)

        setItemsColor(position, view, R.color.label_tertiary, R.color.color_red)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        setItemsColor(position, view, R.color.label_primary, R.color.color_red)

        return view
    }

    private fun setItemsColor(position: Int, view: View, colorLowBasic: Int, colorImportance: Int) {
        when (position) {
            0, 1 -> {
                changeColor(view, R.id.spinner_item, colorLowBasic)
            }
            2 -> {
                changeColor(view, R.id.spinner_item, colorImportance)
            }
        }
    }

    private fun changeColor(view: View, id: Int, color: Int) {
        view.findViewById<TextView>(id).setTextColor(view.context.getColor(color))
    }
}