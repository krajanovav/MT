package com.example.project.data.repositories

import androidx.room.Query
import com.example.project.data.ToDoDao
import com.example.project.data.ToDoDatabase
import com.example.project.data.models.ToDoApp
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao:ToDoDao){

    val getAllTasks: Flow<List<ToDoApp>> = toDoDao.getAllTasks()
    val sortByLowerPriority: Flow<List<ToDoApp>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<ToDoApp>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoApp>{
        return toDoDao.getSelectedTask(taskId = taskId)
    }
    suspend fun addTask(toDoApp: ToDoApp){
        toDoDao.addTask(toDoApp = toDoApp)
    }
    suspend fun updateTask(toDoApp: ToDoApp){
        toDoDao.updateTask(toDoApp=toDoApp)
    }
    suspend fun deleteTask(toDoApp: ToDoApp){
        toDoDao.deleteTask(toDoApp=toDoApp)
    }
    suspend fun deleteAllTasks(){
        toDoDao.deleteAllTasks()
    }
    fun searchDatabase(searchQuery: String): Flow<List<ToDoApp>>{
        return toDoDao.searchDatabase(searchQuery=searchQuery)
    }
}