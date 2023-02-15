package com.android.todoapp.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.todoapp.model.ToDoModel

class ListDiffUtil(
    private val oldList: List<ToDoModel>,
    private val newList: List<ToDoModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].title != newList[newItemPosition].title -> false
            oldList[oldItemPosition].description != newList[newItemPosition].description -> false
            oldList[oldItemPosition].priority != newList[newItemPosition].priority -> false
            else -> true
        }
    }
}
