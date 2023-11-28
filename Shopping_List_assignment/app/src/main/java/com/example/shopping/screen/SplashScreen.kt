package com.example.shopping.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.shopping.R


@Composable
fun SplashScreen(navController : NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(400.dp)
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("shoppingList") {
            popUpTo("splashScreen") { inclusive = true }
        }
    }
}