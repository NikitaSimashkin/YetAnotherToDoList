package com.example.yetanothertodolist.other

import android.content.Context
import android.util.TypedValue

class ConstValues {

    companion object {
        const val TASK_TAG = "Task"
        const val ID_TAG = "id"

        const val DEVICE_ID = "deviceId"
        const val SHARED_PREF_FILE_NAME = "main"
        const val LAST_REVISION_DB = "lastRevision"
        const val LAST_SYNCHRONIZE_TIME = "lst"
        const val START_THEME = "startTheme"
    }

}

fun getColor(context: Context, attrId: Int): Int{
    val color = TypedValue()
    context.theme.resolveAttribute(attrId, color, true)
    return color.data
}