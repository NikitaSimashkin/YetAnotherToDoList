package com.example.yetanothertodolist.ui.stateholders


/**
 * Возможные действия с репозиторием
 */
enum class Action{ // не очень понял, зачем эта прослойка нужна, почему не звать напрямую?
    GetList, UpdateList, Delete, Update, Add, GetElement
}
