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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 검색 http
interface SEARCH {

    @POST("_search")
    suspend fun getSearchResult(@Body body: Any): Response

    companion object {
        private const val BASE_URL = "http://k8a405.p.ssafy.io:8200/"

        fun createSearch(): SEARCH {
            val gson : Gson =   GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SEARCH::class.java)
        }
    }
}

// 검색 결과
data class Response(
    val hits: Hits
)

data class Hits(
    val total: Total,
    val hits: List<SearchItem>
)

data class Total(
    val value: Int
)

data class SearchItem(
    val _source: SearchItemInfo
)

data class SearchItemInfo(
    val item_idx: Int,
    val item_name: String,
    val price: Int,
    val thumbnail: String,
    val inventory: Int
)

@Composable
fun Search(navController: NavController, searchWord: String){
    val coroutineScope = rememberCoroutineScope()
    // 검색할 때 보낼 body
    val searchBody = mapOf(
        "query" to mapOf(
            "match" to mapOf(
                "item_name.nori" to searchWord
            )
        )
    )
    // 검색 결과
    var hits: Hits? by remember { mutableStateOf(null) }

    // 검색 결과 받아오기
    LaunchedEffect(true) {
        try {
            val response: Response = coroutineScope.async { SEARCH.createSearch().getSearchResult(searchBody) }.await()
            hits = response.hits
        } catch (e: Exception) {
            println("검색 결과 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }

    }
    Column() {
        // 상단바
        topBar(navController = navController, "상품 검색")

        // 검색
        searchBar(navController)

        if(hits != null){
            // 검색 결과가 있을 때
            if (hits!!.hits.isNotEmpty()){
                Text("총 ${hits!!.total.value}건의 검색 결과")
                LazyColumn(){
                    items(hits!!.hits){
                        item ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("item/${item._source.item_idx}") }
                        ) {
                            AsyncImage(
                                model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item._source.thumbnail}",
                                contentDescription = "상품 썸네일"
                            )
                            Text(item._source.item_name)
                            Text(text = "${item._source.price}원")
                        }
                    }
                }
            } else{
                Text("일치하는 검색 결과가 없습니다.")
            }
        }
    }
}
