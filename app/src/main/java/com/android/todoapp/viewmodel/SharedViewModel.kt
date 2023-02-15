package com.android.todoapp.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.todoapp.R
import com.android.todoapp.model.Priority
import com.android.todoapp.model.ToDoModel

//@HiltViewModel
class SharedViewModel(application: Application) : AndroidViewModel(application) {

    /* ---------------------------------- CHECK EMPTY DATABASE ---------------------------------- */
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(todoData: List<ToDoModel>) {
        emptyDatabase.value = todoData.isEmpty()
    }

    /* ------------------------------------ PRIORITY COLORS ------------------------------------ */
    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            when (position) {
                0 -> { (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.red)) }

                1 -> { (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.yellow)) }

                2 -> { (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }
    }

    /* --------------------------------- PARSE PRIORITY TO ENUM --------------------------------- */
    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }

    /* --------------------------------- PARSE PRIORITY TO INT --------------------------------- */
    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}
