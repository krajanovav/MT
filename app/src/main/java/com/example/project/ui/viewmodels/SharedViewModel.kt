package com.example.project.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.models.Priority
import com.example.project.data.models.ToDoTask
import com.example.project.data.repositories.DataStoreRepository
import com.example.project.data.repositories.ToDoRepository
import com.example.project.util.Action
import com.example.project.util.RequestState
import com.example.project.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import com.example.project.util.Constants.MAX_TITLE_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks



    fun searchDatabase(searchQuery:String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery="%$searchQuery%")  //sql dotaz používá %
                    .collect{searchedTasks->
                        _searchedTasks.value=RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState=
        MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
               dataStoreRepository.readSortState
                   .map{Priority.valueOf(it)}
                   .collect{
                        _sortState.value = RequestState.Success(it)
                   }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

fun persistSortingState(priority: Priority){
    viewModelScope.launch(Dispatchers.IO){
        dataStoreRepository.persistSortState(priority= priority)
    }
}

    private val _allTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        println("Fetching task with ID: $taskId")
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                println("Task fetched from repository: $task")
                _selectedTask.value = task
            }
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO){
            val toDoTask=ToDoTask(
            title = title.value,
            description=description.value,
            priority=priority.value
            )
            repository.addTask(toDoTask=toDoTask)
        }
        searchAppBarState.value =SearchAppBarState.CLOSED
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO){
            val toDoTask = ToDoTask(
                id = id.value,
                title=title.value,
                description=description.value,
                priority=priority.value
            )
            repository.updateTask(toDoTask=toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO){
            val toDoTask=ToDoTask(
                id = id.value,
                title =title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteTask(toDoTask=toDoTask)
        }
    }

    private fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action){
        when(action){
            Action.ADD ->{
                addTask()
            }
            Action.UPDATE ->{
                updateTask()
            }
            Action.DELETE ->{
                deleteTask()
            }
            Action.DELETE_ALL ->{
                deleteAllTasks()
            }
            Action.UNDO ->{
                addTask()
            }else->{

            }

        }
        this.action.value=Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            println("Updating fields with task: $selectedTask")
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }
    fun updateTitle(newTitle: String){
        if (newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }
    fun validateFields(): Boolean{
        return title.value.isNotEmpty()&& description.value.isNotEmpty()
    }

}