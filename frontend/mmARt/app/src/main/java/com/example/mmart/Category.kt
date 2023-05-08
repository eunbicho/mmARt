package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun Category(navController: NavController, categoryId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var result: List<ItemInfo>? by remember { mutableStateOf(null) }

    // 카테고리 별 아이템 조회
    LaunchedEffect(true) {
        result = coroutineScope.async { api.getCategories(userId, categoryId!!) }.await().result
    }

    fun categoryName(): String{
        return when(categoryId) {
            1 -> "가공식품"
            2 -> "신선식품"
            3 -> "일상용품"
            4 -> "의약품"
            5 -> "교육용품"
            6 -> "디지털"
            7 -> "인테리어"
            8 -> "스포츠"
            else -> "카테고리"
        }
    }

    Column() {

        // result가 null이 아닐 경우만
        if (result != null) {

            // 상단바
            topBar(navController = navController, categoryName())

            LazyColumn(
            ){
                items(result!!){
                        item ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("item/${item.itemIdx}") }
                    ){
                        AsyncImage(
                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                            contentDescription = "상품 썸네일"
                        )

                        Text(text = item.itemName)

                        Text(text = "${item.price}원")
                    }
                }
            }

        }

    }
}
