package com.example.mmart

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

@Composable
fun Search(navController: NavController, searchWord: String){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    var items: List<ItemInfo>? by remember { mutableStateOf(null) }
    // 로딩
    var isLoading: Boolean by remember { mutableStateOf(true) }

    // 검색 결과 받아오기
    LaunchedEffect(true) {
        try {
            items = coroutineScope.async { api.getSearchResult(searchWord) }.await().result
        } catch (e: Exception) {
            println("검색 결과 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 10.dp)) {
        // 상단바
        topBar(navController = navController, "상품 검색")

        Row(
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            searchBar(navController = navController)
        }
        if(items != null){
            // 검색 결과가 있을 때
            if (items!!.isNotEmpty()){
                Text("총 ${items!!.size}건의 검색 결과", modifier=Modifier.padding(start = 20.dp, top = 15.dp))
                items(navController, items!!) {isLoading = it}
            } else{
                isLoading = false
                blankView(msg = "일치하는 검색 결과가 없습니다.")
            }
        }
    }

    if(isLoading){
        loadingView()
    }
}
