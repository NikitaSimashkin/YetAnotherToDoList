package com.example.yetanothertodolist.data.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.yetanothertodolist.ui.view.addFragment.Importance
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.toString() ?: "0"
    }
    @TypeConverter
    fun toLocalDateTime(string: String): LocalDateTime? {
        return if (string == "0")
            null
        else
            LocalDateTime.parse(string)
    }


    @TypeConverter
    fun fromImportance(importance: Importance) = importance.toString()
    @TypeConverter
    fun toImportance(string: String) = Importance.valueOf(string)


    @TypeConverter
    fun fromNullableString(string: String?): String = string?.toString() ?: "0"
    @TypeConverters
    fun toNullableString(string: String): String? = if (string == "0") null else string
}