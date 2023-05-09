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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun Review(itemId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
//    var result: ItemDetail? by remember { mutableStateOf(null) }
//    var reviewWrite: Boolean by remember { mutableStateOf(false) }
    var reviewContent: String by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        // 리뷰 조회
        try {
//            result = coroutineScope.async { api.getItemInfo(itemId!!) }.await().result
        } catch (e: Exception){
            println("리뷰 조회 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 리뷰 작성
    fun write(){
        coroutineScope.async { api.getItemInfo(itemId!!) }
    }

    Column() {
        Row(){
            Text(text = "리뷰")

        }

            TextField(value = reviewContent, onValueChange = { reviewContent = it })
            Button(onClick = { write() }) {
                Text(text = "확인")
            }

    }
}
