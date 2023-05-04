package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun MyPage(navController: NavController, userId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var result: UserInfo? by remember { mutableStateOf(null) }

    // 한 번만 실행
    LaunchedEffect(true) {
       result = coroutineScope.async { api.getUser(userId!!) }.await().result
    }

    Column() {
        Row() {

            Text(text = "마이페이지")

            Button(onClick = { navController.navigate("main") }) {
                Text(text = "메인으로")
            }
        }
        if(result != null){
            Text("${result!!.name}님", fontSize = 20.sp)
        }

    }
}
