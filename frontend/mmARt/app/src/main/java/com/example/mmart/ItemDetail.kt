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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun ItemDetail(navController: NavController, itemId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var result: ItemDetail? by remember { mutableStateOf(null) }
    var reviews: List<ReviewDetail>? by remember { mutableStateOf(null) }

    // 상세 조회
    LaunchedEffect(true) {
        result = coroutineScope.async { api.getItemInfo(itemId!!) }.await().result
        reviews = coroutineScope.async { api.getItemReview(itemId!!) }.await().result
    }

    Column() {

        topBar(navController = navController, "상품 상세")

        // result가 null이 아닐 경우만
        if (result != null) {
            Column(
            ){
                AsyncImage(
                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${result!!.image}",
                    contentDescription = "상품 이미지")
                Text(text = result!!.item.itemName)

                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )


                // 리뷰 부분
                if (reviews != null){
                    Text(text = "리뷰")

                    LazyColumn(){
                        items(reviews!!){
                            review ->
                            Row(){
                                repeat(review.star){
                                    Icon(Icons.TwoTone.Star,"별점", tint = Color.Yellow)
                                }
                                repeat(5-review.star){
                                    Icon(Icons.Outlined.Star,"5-별점")
                                }
                            }
                            Text(review.content)

                            // 내가 작성한 리뷰인 경우, 수정 및 삭제 가능
                            if(review.paymentDetail.user.userIdx == userId) {
                                Button(onClick = { /*TODO*/ }) {
                                    Text("수정하기")
                                }
                                Button(onClick = { /*TODO*/ }) {
                                    Text("삭제하기")
                                }
                            }
                        }
                    }

                } else {
                     Text("작성된 리뷰가 없습니다.")
                }
            }
        }

        }
}
