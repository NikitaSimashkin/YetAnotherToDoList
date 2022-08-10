package com.example.yetanothertodolist.Adapters.TodoAdapterClasses // пакеты принято писать маленькими буквами, разделять точками

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
