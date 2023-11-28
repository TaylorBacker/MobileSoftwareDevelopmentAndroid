package com.example.shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shopping.screen.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import com.example.shopping.ui.theme.ShoppingTheme
import com.example.shopping.screen.TodoListScreen
import com.example.shopping.screen.TodoSummaryScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoAppNavHost()
                }
            }
        }
    }
}

@Composable
fun TodoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splashScreen"
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("splashScreen") {
            SplashScreen(navController = navController)
        }
        composable("shoppingList") {
            TodoListScreen(onNavigateToSummary = { all, important ->
                navController.navigate("todosummary/$all/$important")
            }
            )
        }
        composable("todosummary/{numalltodo}/{numimportant}",
            arguments = listOf(
                navArgument("numalltodo"){type = NavType.IntType},
                navArgument("numimportant"){type = NavType.IntType})
        ) {
            val numalltodo = it.arguments?.getInt("numalltodo")
            val numimportant = it.arguments?.getInt("numimportant")
            val navController = navController
            if (numalltodo != null && numimportant != null) {
                TodoSummaryScreen(
                    numalltodo = numalltodo,
                    numimportanttodo = numimportant,
                    navController = navController )
            }
        }
    }
}


