package com.example.yetanothertodolist.Interactions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.yetanothertodolist.R

object ListInteractions {

    fun onPlusButton() = onView(
        withId(R.id.floatingActionButton)
    )

    fun onList() = onView(
        withId(R.id.recyclerView)
    )

    fun getTextViewIdFromTaskHolder(): Int = R.id.textViewTask

    fun onSnackBar401And500() = onView(
        withText(R.string.retry)
    )

    fun onSnackBarUnknown() = onView(
        withText(R.string.unknownError)
    )
}