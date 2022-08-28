package com.example.yetanothertodolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.yetanothertodolist.data.ListMerger
import com.example.yetanothertodolist.data.repository.TodoItemRepository
import com.example.yetanothertodolist.data.sources.DataBaseSource
import com.example.yetanothertodolist.data.sources.ServerSource
import com.example.yetanothertodolist.other.createRandomTodoItem
import com.example.yetanothertodolist.util.ErrorManager
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever



@RunWith(MockitoJUnitRunner.Silent::class)
class TodoItemRepositoryTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val listMerger: ListMerger = mock(ListMerger::class.java)
    private val dataBaseSource: DataBaseSource = mock(DataBaseSource::class.java)
    private val serverSource: ServerSource = mock(ServerSource::class.java)
    private val errorManager: ErrorManager = mock(ErrorManager::class.java)

    @Before
    fun setDispatcher() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun deleteDispatcher() {
        Dispatchers.resetMain()
    }


    @Test
    fun shouldAddTaskToListAndCallToDataBaseSourceAndServerSource() = runTest {
        // given
        val list = mutableListOf(createRandomTodoItem(), createRandomTodoItem())
        val item = createRandomTodoItem()
        val repository = TodoItemRepository(
            listMerger = listMerger,
            dataBaseSource = dataBaseSource,
            serverSource = serverSource
        )

        whenever(listMerger.merge(any(), any())).thenReturn(list)
        whenever(errorManager.launchWithHandler(any())).thenAnswer {
            runBlocking {
                (it.arguments[0] as (suspend () -> Unit)).invoke()
            }
        }

        repository.errorManager = errorManager
        repository.updateList()

        // when
        list.add(item)
        repository.addItem(item)

        // then
        assertEquals(list, repository.tasks.value)
        verify(dataBaseSource, times(1)).addItem(item)
        verify(serverSource, times(1)).addItem(item)
    }


    @Test
    fun shouldDeleteTaskFromListAndCallToDataBaseSourceAndServerSource() = runTest {
        // given
        val list = mutableListOf(createRandomTodoItem(), createRandomTodoItem())
        val item = list[0]
        val repository = TodoItemRepository(
            listMerger = listMerger,
            dataBaseSource = dataBaseSource,
            serverSource = serverSource
        )

        whenever(listMerger.merge(any(), any())).thenReturn(list)
        `when`(errorManager.launchWithHandler(any())).thenAnswer {
            runBlocking {
                (it.arguments[0] as (suspend () -> Unit)).invoke()
            }
        }

        repository.updateList()
        repository.errorManager = errorManager

        // when
        list.remove(item)
        repository.removeItem(item)

        // then
        assertEquals(list, repository.tasks.value)
        verify(dataBaseSource, times(1)).deleteItem(item)
        verify(serverSource, times(1)).deleteItem(item.id)
    }
}