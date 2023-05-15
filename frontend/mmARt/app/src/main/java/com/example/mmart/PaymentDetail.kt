package com.example.mmart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Vivid_blue
import kotlinx.coroutines.async

@Composable
fun PaymentDetail(navController: NavController, paymentIdx: Int) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var paymentDetails: List<PaymentDetail>? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        try {
            paymentDetails = coroutineScope.async { api.getPaymentDetails(userId, paymentIdx) }.await().result
            println(paymentDetails.toString())
        } catch (e: Exception) {
            println("결제 내역 상세 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    Column {
        // 상단바
        topBar(navController, "결제 내역 상세 조회")

        if (paymentDetails != null) {
            LazyColumn {
                items(paymentDetails!!) { paymentDetail ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 23.dp, start = 23.dp, end = 23.dp)
                            .border(width = (1.8).dp, color = Main_gray, shape = RoundedCornerShape(11.dp))
                    ) {
                        // 상품 정보
                        Box(
                            modifier = Modifier
                                .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                                .fillMaxWidth()
                                .clickable { navController.navigate("item/${paymentDetail.item.itemIdx}") }
                        ) {
                            Row {
                                AsyncImage(
                                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail.item.thumbnail}",
                                    contentDescription = "상품 썸네일",
                                    modifier = Modifier.size(75.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp)
                                ) {
                                    Text(
                                        text = paymentDetail.item.itemName,
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    )
                                    if (!paymentDetail.item.isCoupon) {
                                        Text(
                                            text = "${paymentDetail.quantity}개 ${paymentDetail.totalPrice}원",
                                            modifier = Modifier.padding(top = 5.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${paymentDetail.quantity}개 ${paymentDetail.item.couponPrice * paymentDetail.quantity}원",
                                            modifier = Modifier.padding(top = 5.dp)
                                        )
                                    }

                                }
                            }
                            // 작성된 리뷰가 없을 경우
                            if (!paymentDetail.isWriteReview) {
                                OutlinedButton(
                                    border = BorderStroke(color = Vivid_blue, width = 2.dp),
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 15.dp),
                                    onClick = { navController.navigate("reviewCreate/${paymentDetail.paymentDetailIdx}") },
                                    elevation = ButtonDefaults.elevation(2.dp)

                                ) {
                                    Text(
                                        text = "리뷰 작성",
                                        color = Main_gray,
                                    )
                                }
                            }


                        }
                    }
                }
            }
        }
    }
}
