package com.example.project.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.project.data.models.ToDoApp
import kotlinx.coroutines.flow.Flow


@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTasks(): Flow <List<ToDoApp>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId" )
    fun getSelectedTask(taskId: Int): Flow<ToDoApp>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoApp: ToDoApp)

    @Update
    suspend fun updateTask(toDoApp: ToDoApp)

    @Delete
    suspend fun deleteTask(toDoApp: ToDoApp)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String) : Flow<List<ToDoApp>>


    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE '%M' THEN 2 WHEN priority LIKE 'H%' THEN 3 END ")
    fun sortByLowPriority(): Flow<List<ToDoApp>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE '%M' THEN 2 WHEN priority LIKE 'L%' THEN 3 END ")
    fun sortByHighPriority(): Flow<List<ToDoApp>>
}