package com.example.mmart

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun Category(navController: NavController, categoryId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var result: List<ItemInfo>? by remember { mutableStateOf(null) }

    // 한 번만 실행
    LaunchedEffect(true) {
        result = coroutineScope.async { api.getCategories(1, categoryId!!) }.await().result
    }


    Column() {
        Row() {

            Text(text = "categotyId = ${categoryId}")

            Button(onClick = { navController.navigate("main") }) {
                Text(text = "메인으로")
            }
        }
        if (result != null) {
            LazyColumn{
                items(result!!){
                        item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        AsyncImage(
                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                            contentDescription = item.itemName
                        )

                        Text(text = item.itemName)

                        Text(text = "${item.price}원")
                    }
                }
            }

        }

    }
}
