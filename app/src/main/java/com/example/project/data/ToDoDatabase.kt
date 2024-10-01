package com.example.project.data;

import androidx.room.Database;
import androidx.room.RoomDatabase
import com.example.project.data.models.ToDoApp


@Database(entities = [ToDoApp::class], version = 1, exportSchema = false )

abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao():ToDoDao


}
