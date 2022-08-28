package com.example.yetanothertodolist.Interactions

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.yetanothertodolist.R
import org.hamcrest.Matchers


object AddInteractions {

    fun onDescription() = onView(
        withId(R.id.description)
    )

    fun onImportance() = onView(
        withId(R.id.spinner)
    )

    fun onSwitch() = onView(
        withId(R.id.calendar_switch)
    )

    fun onDatePicker() = onView(
        withClassName(Matchers.equalTo(DatePicker::class.java.name))
    )

    fun onSaveButton() = onView(
        withId(R.id.save)
    )

    fun onDeleteButton() = onView(
        withId(R.id.delete)
    )

    fun onImportanceLow() = onView(
        withText(R.string.low)
    )

    fun onReady() = onView(
        withText(R.string.ready)
    )

    fun onSnackBar() = onView(
        withText(R.string.save_error)
    )

    fun onCancelButton() = onView(
        withId(R.id.close)
    )
}