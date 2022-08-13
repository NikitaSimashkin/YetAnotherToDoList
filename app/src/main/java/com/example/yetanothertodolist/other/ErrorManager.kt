package com.example.yetanothertodolist.other


interface ErrorManager {
    suspend fun launchWithHandler(action: (suspend ()->Unit)? = null)
}