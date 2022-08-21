package com.example.yetanothertodolist.util


interface ErrorManager {
    suspend fun launchWithHandler(action: (suspend ()->Unit)? = null)
}