package com.example.mmart

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.*

@Composable
fun Category(navController: NavController, categoryIdx: Int){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    // 아이템 리스트
    var items: List<ItemInfo>? by remember { mutableStateOf(null) }

    // 로딩
    var isLoading: Boolean by remember { mutableStateOf(true) }

    // 카테고리 별 아이템 조회
    LaunchedEffect(true) {
        try {
            items = coroutineScope.async { api.getCategories(userId, categoryIdx) }.await().result
        } catch (e: Exception){
            println("카테고리 별 상품 조회 에러-----------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    val categoryName = when(categoryIdx) {
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

    Column() {

        // 상단바
        topBar(navController = navController, categoryName)

        // result가 null이 아닐 경우만
        if (items != null) {
            items(navController, items!!) {isLoading = it}
        }
    }

    if(isLoading){
        loadingView()
    }
}
