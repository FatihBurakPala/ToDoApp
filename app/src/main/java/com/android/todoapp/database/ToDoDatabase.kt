package com.android.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.todoapp.model.ToDoModel

@Database(entities = [ToDoModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun todoDao(): ToDoDao
}
