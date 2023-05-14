package com.example.mmart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Main_yellow
import kotlinx.coroutines.*

@Composable
fun ReviewCreate(navController: NavController, paymentDetailIdx: Int){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    // 결제 정보
    var paymentDetail: PaymentDetail? by remember { mutableStateOf(null) }
    // 별점
    var reviewStar: Int by remember { mutableStateOf(0) }
    // 내용
    var reviewContent: String by remember { mutableStateOf("") }
    // 별점 확인 모달
    var starCheck: Boolean by remember { mutableStateOf(false) }

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
        val reviewBody = mapOf(
            "star" to reviewStar,
            "content" to reviewContent
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

//        if(paymentDetail != null){
//
//            // 상품 정보
//            Row(
//                modifier = Modifier.clickable { navController.navigate("item/${paymentDetail!!.item.itemIdx}") }
//            ) {
//                AsyncImage(model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail!!.item.thumbnail}", contentDescription = "상품 썸네일")
//                Text(paymentDetail!!.item.itemName)
//            }
//
//            Row(){
//                listOf(1, 2, 3, 4, 5).forEach {
//                    Icon(
//                        imageVector = Icons.Filled.Star,
//                        contentDescription = "별점",
//                        tint = if (it <= reviewStar) Color.Yellow else Color.LightGray,
//                        modifier = Modifier
//                            .clickable { reviewStar = it}
//                    )
//                }
//
//            }
//
//            // 내용 입력
//
//            OutlinedTextField(
//                value = reviewContent,
//                onValueChange = { reviewContent = it },
//            )
//
//            // 확인 및 취소 버튼
//            Row(){
//                Button(onClick = {
//                    if(reviewStar != 0){reviewCreate()}else{ starCheck = true} }) {
//                    Text("확인")
//                }
//                Button(onClick = { navController.popBackStack() }) {
//                    Text("취소")
//                }
//            }
//        }
        if (paymentDetail != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(23.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .border(shape = RoundedCornerShape(11.dp), border = BorderStroke((1.8).dp, Main_gray))
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp)
                ) {
                    Column {
                        // 상품 정보
                        Row(
                            modifier = Modifier
                                .clickable { navController.navigate("item/${paymentDetail!!.item.itemIdx}") }
                        ) {
                            AsyncImage(
                                model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail!!.item.thumbnail}",
                                contentDescription = "상품 썸네일",
                                modifier = Modifier.size(75.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = paymentDetail!!.item.itemName,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                                Text(
                                    text = paymentDetail!!.createTime.toString(),
                                    color = Main_gray,
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }

                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 15.dp)
                        )

                        Row {
                            listOf(1, 2, 3, 4, 5).forEach {
                                Icon(imageVector = Icons.Filled.Star,
                                    contentDescription = "별점",
                                    tint = if (it <= reviewStar) Color.Yellow else Color.LightGray,
                                    modifier = Modifier.clickable { reviewStar = it })
                            }

                        }

                        // 내용 입력

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .fillMaxWidth()
                                .border(width = (1.8).dp, color = Color.Transparent),
                            value = reviewContent,
                            onValueChange = { reviewContent = it },
                        )

                        // 확인 및 취소 버튼
                        Row(
                            modifier = Modifier.align(Alignment.End).padding(top = 5.dp)
                        ) {
                            OutlinedButton(
                                modifier = Modifier.padding(start = 5.dp, end = 10.dp, bottom = 10.dp),
                                elevation = ButtonDefaults.elevation(2.dp),
                                border = BorderStroke(color = Main_blue, width = 2.dp),
                                onClick = { reviewCreate() }
                            ) {
                                Text("확인", color = Main_gray)
                            }
                            OutlinedButton(
                                modifier = Modifier.padding(start = 5.dp, bottom = 10.dp),
                                elevation = ButtonDefaults.elevation(2.dp),
                                border = BorderStroke(color = Main_yellow, width = 2.dp),
                                onClick = { navController.popBackStack() }
                            ) {
                                Text("취소", color = Main_gray)
                            }
                        }
                    }
                }
            }


        }


        if(starCheck){
            AlertDialog(
                onDismissRequest = { starCheck = false },
                title = { Text("별점 확인") },
                text = { Text("별점을 선택해 주세요.") },
                // 확인 버튼
                confirmButton = {
                    Button(onClick = { starCheck = false }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}
