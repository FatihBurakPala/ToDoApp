package com.android.todoapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.todoapp.utils.Constants.DATABASE_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = DATABASE_TABLE)
@Parcelize
data class ToDoModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var priority: Priority,
    var description: String
): Parcelable
