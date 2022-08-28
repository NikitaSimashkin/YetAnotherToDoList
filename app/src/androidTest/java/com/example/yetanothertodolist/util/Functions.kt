package com.example.yetanothertodolist.util

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Description
import org.hamcrest.Matcher

fun atLastPosition(matcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("has item at position last position: ");
            matcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView): Boolean {
            val holderNumber = item.adapter?.itemCount ?: return false
            val holder = item.findViewHolderForAdapterPosition(holderNumber - 1) ?: return false

            return matcher.matches(holder.itemView)
        }

    }
}

// функция для получения текста из view
fun getText(str: Array<String>, textViewId: Int) = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(View::class.java)
    }

    override fun getDescription(): String {
        return "Get text from textView"
    }

    override fun perform(uiController: UiController?, view: View?) {
        str[0] = view?.findViewById<TextView>(textViewId)?.text.toString()
    }
}
