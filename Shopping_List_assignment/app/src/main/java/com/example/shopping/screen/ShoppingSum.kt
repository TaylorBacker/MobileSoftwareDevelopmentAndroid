@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shopping.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun TodoSummaryScreen(
    modifier: Modifier = Modifier,
    numalltodo: Int,
    numimportanttodo: Int,
    navController: NavController
) {
    TopAppBar(
        title = {
        Text("Shopping List")
    },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        actions = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        })

    Column(modifier = modifier
        .fillMaxSize()
        .padding(start = 40.dp, end = 40.dp), verticalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Total: $numalltodo",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Text(
                text = "Bought Already: $numimportanttodo",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        }
        Image(
            painter = painterResource(
                com.example.shopping.R.drawable.bracket
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )

        Text(
            text = "Done: ${numalltodo - numimportanttodo}",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}