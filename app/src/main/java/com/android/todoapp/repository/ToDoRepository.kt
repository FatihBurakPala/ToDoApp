package com.android.todoapp.repository

import com.android.todoapp.database.ToDoDao
import com.android.todoapp.model.ToDoModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val todoDao: ToDoDao) {

    val getAllData: Flow<List<ToDoModel>> = todoDao.getAllData()
    val sortByHighPriority: Flow<List<ToDoModel>> = todoDao.sortByHighPriority()
    val sortByLowPriority: Flow<List<ToDoModel>> = todoDao.sortByLowPriority()

    suspend fun insertData(todo: ToDoModel) {
        todoDao.insertData(todo)
    }

    suspend fun updateData(todo: ToDoModel) {
        todoDao.updateData(todo)
    }

    suspend fun deleteData(todo: ToDoModel) {
        todoDao.deleteData(todo)
    }

    suspend fun deleteAllData() {
        todoDao.deleteAllData()
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoModel>> {
        return todoDao.searchDatabase(searchQuery)
    }
}
