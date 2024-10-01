package com.example.project.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.models.ToDoApp
import com.example.project.data.repositories.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository
): ViewModel() {

    private val _allTasks =
        MutableStateFlow<List<ToDoApp>>(emptyList())
    val allTasks: StateFlow<List<ToDoApp>> = _allTasks

    fun getAllTasks(){ //call repository function
        viewModelScope.launch {
            repository.getAllTasks.collect{

            }
        }
    }
}