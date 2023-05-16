package com.example.mmart

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Dark_gray
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Vivid_blue
import kotlinx.coroutines.async
import java.text.DecimalFormat

@Composable
fun PaymentDetail(navController: NavController, paymentIdx: Int) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var payment: Payment? by remember { mutableStateOf(null) }
    var paymentDetails: List<PaymentDetail>? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        try {
            payment = coroutineScope.async { api.getPayment(paymentIdx, userId) }.await().result
            paymentDetails = coroutineScope.async { api.getPaymentDetails(userId, paymentIdx) }.await().result
        } catch (e: Exception) {
            println("결제 내역 상세 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    Column {
        // 상단바
        topBar(navController, "결제 내역 상세")

        if (payment != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 30.dp, 30.dp, 0.dp)
                    .paint(painter = painterResource(R.drawable.receipt_top))
            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Image(painter = painterResource(R.drawable.mmart_logo), contentDescription = "mmart", Modifier.size(80.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = "싸피마트 ${payment!!.marketName}점",
                        color = Main_gray,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "${payment!!.date[0]}-${payment!!.date[1]}-${payment!!.date[2]} ${payment!!.date[3]}:${payment!!.date[4]}",
                        color = Main_gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Divider(
                thickness = 2.dp,
                color = Dark_gray,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
            )
        }
            if (paymentDetails != null) {
                LazyColumn {
                    items(paymentDetails!!) { paymentDetail ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 0.dp)
                                .paint(painter = painterResource(R.drawable.receipt_content)),
//                                .border(
//                                    width = (1.8).dp,
//                                    color = Main_gray,
//                                    shape = RoundedCornerShape(24.dp)
//                                )
                        ) {
                            // 상품 정보
                            Box(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
//                                    .clickable { navController.navigate("item/${paymentDetail.item.itemIdx}") }
                            ) {
                                Column(

                                ) {
                                    Row(
                                        modifier = Modifier.padding(15.dp, 0.dp)
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .size(64.dp, 64.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .border(
                                                    color = Dark_gray,
                                                    width = 1.5.dp,
                                                    shape = RoundedCornerShape(10.dp)
                                                )
                                                .clickable { navController.navigate("item/${paymentDetail.item.itemIdx}") },
                                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail.item.thumbnail}",
                                            contentDescription = paymentDetail.item.itemName,
                                        )
                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(start = 10.dp)
                                        ) {
                                            Text(
                                                text = paymentDetail.item.itemName,
                                                fontSize = 15.sp,
                                                modifier = Modifier.padding(bottom = 5.dp)
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.width(210.dp),
                                            ) {
                                                Text(
                                                    text = DecimalFormat("#,###").format(
                                                        paymentDetail.item.price
                                                    ),
                                                    fontWeight = FontWeight.Light,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = paymentDetail.quantity.toString(),
                                                    fontWeight = FontWeight.Light,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = DecimalFormat("#,###").format(
                                                        paymentDetail.item.price * paymentDetail.quantity
                                                    ),
                                                    fontWeight = FontWeight.Light,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            if (paymentDetail.item.isCoupon) {
                                                Text(
                                                    text = DecimalFormat("#,###").format(
                                                        paymentDetail.totalPrice
                                                    ),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    color = Vivid_blue,
                                                    modifier = Modifier.align(Alignment.End),
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.padding(20.dp, 0.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // 작성된 리뷰가 없을 경우
                                        if (!paymentDetail.isWriteReview) {
                                            OutlinedButton(
                                                border = BorderStroke(
                                                    color = Vivid_blue,
                                                    width = 2.dp,
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                onClick = { navController.navigate("reviewCreate/${paymentDetail.paymentDetailIdx}") },
                                                elevation = ButtonDefaults.elevation(2.dp)

                                            ) {
                                                Text(
                                                    text = "리뷰 작성",
                                                    color = Main_gray,
                                                )
                                            }
                                        } else {
                                            OutlinedButton(
                                                border = BorderStroke(
                                                    color = Vivid_blue,
                                                    width = 2.dp
                                                ),
                                                modifier = Modifier.width(150.dp).padding(end = 5.dp),
                                                onClick = { navController.navigate("reviewUpdate/${paymentDetail.paymentDetailIdx}") },
                                                elevation = ButtonDefaults.elevation(2.dp)

                                            ) {
                                                Text(
                                                    text = "리뷰 수정",
                                                    color = Main_gray,
                                                )
                                            }
                                            OutlinedButton(
                                                border = BorderStroke(
                                                    color = Vivid_blue,
                                                    width = 2.dp
                                                ),
                                                modifier = Modifier.width(150.dp).padding(start = 5.dp),
                                                onClick = { navController.navigate("reviewCreate/${paymentDetail.paymentDetailIdx}") },
                                                elevation = ButtonDefaults.elevation(2.dp)

                                            ) {
                                                Text(
                                                    text = "리뷰 삭제",
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
        }
    }
}
