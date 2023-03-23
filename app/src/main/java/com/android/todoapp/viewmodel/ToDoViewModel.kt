package com.android.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.todoapp.model.ToDoModel
import com.android.todoapp.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    init {
        getAllData()
    }

    /* ------------------------------ Get All Data ------------------------------ */
    private var _getAllData = MutableStateFlow<List<ToDoModel>>(emptyList())
    val getAllData: StateFlow<List<ToDoModel>> = _getAllData

    private fun getAllData() = viewModelScope.launch {
        repository.getAllData.collect { _getAllData.value = it }
    }

    /* ----------------------- Sort Data By High Priority ----------------------- */
    private var _sortByHighPriority = MutableStateFlow<List<ToDoModel>>(emptyList())
    val sortByHighPriority: StateFlow<List<ToDoModel>> = _sortByHighPriority

    fun sortByHighPriority() = viewModelScope.launch {
        repository.sortByHighPriority.collect { _sortByHighPriority.value = it }
    }

    /* ----------------------- Sort Data By Low Priority ----------------------- */
    private var _sortByLowPriority = MutableStateFlow<List<ToDoModel>>(emptyList())
    val sortByLowPriority: StateFlow<List<ToDoModel>> = _sortByLowPriority

    fun sortByLowPriority() = viewModelScope.launch {
        repository.sortByLowPriority.collect { _sortByLowPriority.value = it }
    }

    fun insertData(todoData: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todoData)
        }
    }

    fun updateData(todoData: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(todoData)
        }
    }

    fun deleteItem(todoData: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(todoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoModel>> {
        return repository.searchDatabase(searchQuery)
    }
}
