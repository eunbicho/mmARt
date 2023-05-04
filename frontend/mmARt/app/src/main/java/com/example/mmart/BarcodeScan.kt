package com.example.mmart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun BarcodeScan(navController: NavController){
    LaunchedEffect(true){
    }

    Row() {

        Text(text = "바코드 스캔")

        Button(onClick = { navController.navigate("main") }) {
            Text(text = "메인으로")
        }
    }

}
