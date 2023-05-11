package com.example.mmart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun ReviewCreate(navController: NavController, paymentDetailIdx: Int){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    // 결제 정보
    var paymentDetail: PaymentDetail? by remember { mutableStateOf(null) }
    // 별점
    var reviewStar: Int by remember { mutableStateOf(1) }
    // 내용
    var reviewContent: String by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        // 결제 정보 조회
        try {
            paymentDetail = coroutineScope.async { api.getPaymentDetail(paymentDetailIdx) }.await().result
        } catch (e: Exception){
            println("리뷰 작성 - 결제 정보 조회 에러-------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    fun reviewCreate() {
        val reviewBody = ReviewBody(
            reviewStar,
            reviewContent
        )

        // 리뷰 작성
        try{
            coroutineScope.async { api.createReview(userId, paymentDetailIdx, reviewBody) }
        } catch (e: Exception){
            println("리뷰 작성 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }

        // 이전 페이지로 돌아감
        navController.popBackStack()
    }

    Column() {
        // 상단바
        topBar(navController, "리뷰 작성")

        if(paymentDetail != null){

            // 상품 정보
            Row(
                modifier = Modifier.clickable { navController.navigate("item/${paymentDetail!!.item.itemIdx}") }
            ) {
                AsyncImage(model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail!!.item.thumbnail}", contentDescription = "상품 썸네일")
                Text(paymentDetail!!.item.itemName)
                Text("${paymentDetail!!.quantity}개 ${paymentDetail!!.totalPrice}")
            }

            Row(){
                listOf(1, 2, 3, 4, 5).forEach {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "별점",
                        tint = if (it <= reviewStar) Color.Yellow else Color.LightGray,
                        modifier = Modifier
                            .clickable { reviewStar = it}
                    )
                }

            }

            // 내용 입력

            OutlinedTextField(
                value = reviewContent,
                onValueChange = { reviewContent = it },
            )

            // 확인 및 취소 버튼
            Row(){
                Button(onClick = { reviewCreate() }) {
                    Text("확인")
                }
                Button(onClick = { navController.popBackStack() }) {
                    Text("취소")
                }
            }
        }
    }
}
