package com.example.yetanothertodolist.Adapters.TodoAdapterClasses

import java.lang.IllegalArgumentException

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