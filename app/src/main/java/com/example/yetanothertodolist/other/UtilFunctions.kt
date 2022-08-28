package com.example.yetanothertodolist.other

import android.content.Context
import android.util.TypedValue
import com.example.yetanothertodolist.data.model.TodoItem
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.time.LocalDateTime
import java.util.*

fun getColor(context: Context, attrId: Int): Int{
    val color = TypedValue()
    context.theme.resolveAttribute(attrId, color, true)
    return color.data
}

fun createRandomTodoItem(): TodoItem {
    val r = Random(System.currentTimeMillis())
    return TodoItem(
        id = UUID.randomUUID().toString(),
        description = UUID.randomUUID().toString(),
        importance = getImportance(),
        done = (System.currentTimeMillis() % 2 == 0L),
        createdAt = LocalDateTime.of(
            r.nextInt(3) + 2020,
            r.nextInt(12) + 1,
            r.nextInt(28) + 1,
            0,
            0,
            0
        ),
        deadline = LocalDateTime.MAX,
        changedAt = LocalDateTime.now(),
        color = null,
        lastUpdateBy = "me",
        isDeleted = false
    )
}

private fun getImportance(): Importance {
    val a = System.currentTimeMillis()
    if (a % 3 == 0L)
        return Importance.Low
    if (a % 3 == 1L)
        return Importance.Basic
    return Importance.Important
}