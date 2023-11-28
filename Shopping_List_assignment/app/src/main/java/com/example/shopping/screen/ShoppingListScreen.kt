package com.example.shopping.screen
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping.data.TodoItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel = hiltViewModel(),
    onNavigateToSummary: (Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val todoList by todoViewModel.getAllToDoList().collectAsState(emptyList())
    var showAddTodoDialog by rememberSaveable { mutableStateOf(false)  }
    var todoToEdit: TodoItem? by rememberSaveable {  mutableStateOf(null) }


    Column {

        TopAppBar(
            title = {
                Text("Shopping List")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    
                }
                IconButton(onClick = {
                    todoViewModel.clearAllTodos()
                }) {
                    Icon(Icons.Filled.Delete, null)
                }
                IconButton(onClick = {
                    coroutineScope.launch {
                        val allTodos = todoViewModel.getAllTodoNum()
                        val importantTodos = todoViewModel.getImportantTodoNum()
                        onNavigateToSummary(
                            allTodos,
                            importantTodos
                        )
                    }
                }) {
                    Icon(Icons.Filled.Info, null)
                }
                IconButton(onClick = {
                    todoToEdit = null
                    showAddTodoDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            })

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddTodoDialog) {
                AddNewTodoForm(
                    todoViewModel,
                    { showAddTodoDialog = false },
                    todoToEdit
                )
            }

            if (todoList.isEmpty())
                Text(text = "No items")
            else {
                LazyColumn(modifier = Modifier) {
                    items(todoList) {
                        TodoCard(todoItem = it,
                            onRemoveItem = { todoViewModel.removeTodoItem(it) },
                            onTodoCheckChange = { checkState ->
                                todoViewModel.changeTodoState(it, checkState)
                            },
                            onEditItem = { editedTodoItem ->
                                todoToEdit = editedTodoItem
                                showAddTodoDialog = true
                            }
                        )
                    }
                }
            }
        }
        }
    }


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddNewTodoForm(
    todoViewModel: TodoViewModel,
    onDialogDismiss: () -> Unit = {},
    todoToEdit: TodoItem? = null
) {
    Dialog(
        onDismissRequest = onDialogDismiss
    ) {
        var name by rememberSaveable { mutableStateOf(todoToEdit?.title ?: "") }
        var description by rememberSaveable { mutableStateOf(todoToEdit?.description ?: "") }
        var price by rememberSaveable { mutableStateOf(todoToEdit?.price ?: "") }
        var bought by rememberSaveable { mutableStateOf(false) }
        var category by rememberSaveable { mutableStateOf( "") }
        var nameError by remember { mutableStateOf(false) }
        var descriptionError by remember { mutableStateOf(false) }
        var priceError by remember { mutableStateOf(false) }

        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp)
        ) {


            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name ,
                onValueChange = { name = it
                    nameError = it. isBlank()
                },
                label = { Text(text = "Enter Shopping Item Here:") },
                isError = nameError
            )
            if (nameError) Text(("Please Write Name"), color = Color.Red)
            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = price,
                onValueChange = {
                    price = it
                    priceError = it.isBlank()
                },
                label = { Text(text = "Enter Price Here:") },
                isError = priceError
            )
            if (priceError) Text(("Please Write Price"), color = Color.Red)
            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = it.isBlank()
                },
                label = { Text(text = "Description:") },
                isError = descriptionError
            )
            if (descriptionError) Text(("Please Description Name"), color = Color.Red)
            Spacer(modifier = Modifier.size(8.dp))

            SpinnerSample(
                listOf("Meat", "Dairy", "Produce"),
                preselected = category,
                onSelectionChanged = {category = it } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = bought, onCheckedChange = {
                    bought= it
                })
                Text(text = "Bought?")
            }

            Row {
                Button(onClick = {
                    nameError = name.isBlank()
                    descriptionError = description.isBlank()
                    priceError = price.isBlank()
                    if (todoToEdit == null) {
                        if (!nameError && !descriptionError && !priceError) {
                            val newTodo = TodoItem(
                                0,
                                name,
                                description,
                                price,
                                category,
                                bought
                            )
                            todoViewModel.addTodoList(newTodo)
                        }
                    } else {
                        val todoEdited = todoToEdit.copy(
                            title = name
                        )
                        todoViewModel.editTodoItem(todoEdited)
                    }
                    onDialogDismiss()
                }) {
                    Text(text = "Save")
                }
            }

            }
        }
    }

@Composable
fun TodoCard(
    todoItem: TodoItem,
    onTodoCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (TodoItem) -> Unit = {}
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring()
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = todoItem.getIcon()),
                    contentDescription = "Type",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )

                Text(todoItem.title, modifier = Modifier.fillMaxWidth(0.2f))
                Spacer(modifier = Modifier.fillMaxSize(0.35f))
                Checkbox(
                    checked = todoItem.isDone,
                    onCheckedChange = { onTodoCheckChange(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        onEditItem(todoItem)
                    },
                    tint = Color.Blue
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }
            }

            if (expanded) {
                Text(
                    text = todoItem.description,
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
                Text(
                    text = todoItem.price,
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
            }
        }
    }
}


@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(
                Icons.Outlined.ArrowDropDown, null, modifier =
                Modifier.padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}