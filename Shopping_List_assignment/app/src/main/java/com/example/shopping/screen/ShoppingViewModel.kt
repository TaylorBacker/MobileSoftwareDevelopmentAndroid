package com.example.shopping.screen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.shopping.data.TodoDAO
import com.example.shopping.data.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoDAO: TodoDAO
) : ViewModel() {


    fun getAllToDoList(): Flow<List<TodoItem>> {
        return todoDAO.getAllTodos()
    }

    suspend fun getAllTodoNum(): Int {
        return todoDAO.getTodosNum()
    }

    suspend fun getImportantTodoNum(): Int {
        return todoDAO.getImportantTodosNum()
    }

    fun addTodoList(todoItem: TodoItem) {
        viewModelScope.launch {
            todoDAO.insert(todoItem)
        }
    }


    fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoDAO.delete(todoItem)
        }
    }

    fun editTodoItem(editedTodo: TodoItem) {
        viewModelScope.launch {
            todoDAO.update(editedTodo)
        }
    }

    fun changeTodoState(todoItem: TodoItem, value: Boolean) {
        val newTodoItem = todoItem.copy()
        newTodoItem.isDone = value
        viewModelScope.launch {
            todoDAO.update(newTodoItem)
        }
    }

    fun clearAllTodos() {
        viewModelScope.launch {
            todoDAO.deleteAllTodos()
        }
    }
}