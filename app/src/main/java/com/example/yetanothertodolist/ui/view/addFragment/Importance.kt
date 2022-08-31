package com.example.yetanothertodolist.ui.view.addFragment

import android.content.Context
import com.example.yetanothertodolist.R

/**
 * Вариации важности задания
 */
enum class Importance {
    Low, Basic, Important;

    companion object {
        fun getImportance(i: Int): Importance = when (i) {
            0 -> Low
            1 -> Basic
            2 -> Important
            else -> throw IllegalArgumentException()
        }

        fun getTranslatedName(importance: Importance, context: Context) =
            when (importance) {
                Low -> context.getText(R.string.low)
                Basic -> context.getText(R.string.basic)
                Important -> context.getText(R.string.important)
            }
    }
}