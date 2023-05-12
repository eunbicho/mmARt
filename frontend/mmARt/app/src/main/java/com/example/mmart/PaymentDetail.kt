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
fun PaymentDetail(navController: NavController, paymentIdx: Int){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var paymentDetails: List<PaymentDetail>? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        try {
            paymentDetails = coroutineScope.async { api.getPaymentDetails(userId, paymentIdx) }.await().result
        } catch (e: Exception){
            println("결제 내역 상세 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    Column() {
        // 상단바
        topBar(navController, "결제 내역 상세 조회")

        if(paymentDetails != null) {
            LazyColumn(){
                items(paymentDetails!!){
                        paymentDetail ->
                    Column() {
                        // 상품 정보
                        Row(){
                            AsyncImage(model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail.item.thumbnail}", contentDescription = "상품 썸네일")
                            Column() {
                                Text(paymentDetail.item.itemName)
                                Text("${paymentDetail.quantity}개  ${paymentDetail.totalPrice}")
                            }
                        }
                        // 작성된 리뷰가 없을 경우
                        if(!paymentDetail.isWriteReview){
                            Button(onClick = { navController.navigate("reviewCreate/${paymentDetail.paymentDetailIdx}") }) {
                                Text("리뷰 작성하기")
                            }
                        }

                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(10.dp)
                        )
                    }

                }
            }
        }
    }
}
