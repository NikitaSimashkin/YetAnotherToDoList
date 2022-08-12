package com.example.yetanothertodolist.ui.view.addFragment

import java.lang.IllegalArgumentException

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
    }
}