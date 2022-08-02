package com.example.yetanothertodolist.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.yetanothertodolist.R


/*
Адаптер для выпадающего списка на AddFragment
 */
class ImportanceAdapter(context: Context, resource: Int, textViewResourceId: Int) :
    ArrayAdapter<String>(context, resource, textViewResourceId) {

    var list: List<String> = context.resources.getStringArray(R.array.importance).toList()

    init{
        addAll(list)
    }


    // метод для выбранного из выпадающего списка view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =  super.getView(position, convertView, parent)

        // убираем отступ слева
        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)

        when (position){
            0, 1->{
                changeColor(view, R.id.spinner_item, R.color.label_tertiary)
            }
            list.size-1->{
                changeColor(view, R.id.spinner_item, R.color.color_red)
            }
        }
        return view
    }


    // метод для view в выпадающем списке
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        when (position){
            0, 1->{
                changeColor(view, R.id.spinner_item, R.color.label_primary)
            }
            list.size-1->{
                changeColor(view, R.id.spinner_item, R.color.color_red)
            }
        }
        return view
    }

    private fun changeColor(view: View, id:Int,  color: Int){
        view.findViewById<TextView>(id).setTextColor(view.context.getColor(color))
    }
}