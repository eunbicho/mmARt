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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Main_yellow
import com.example.mmart.ui.theme.Vivid_yellow
import kotlinx.coroutines.*

@Composable
fun ReviewSave(navController: NavController, paymentDetailIdx: Int, reviewIdx: Int){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    // 리뷰 생성인지, 수정인지 여부
    val isCreate: Boolean = paymentDetailIdx!=0 && reviewIdx==0

    // 생성 - 결제 정보
    var paymentDetail: PaymentDetail? by remember { mutableStateOf(null) }
    // 수정 - 리뷰 정보
    var review: ReviewDetail? by remember { mutableStateOf(null) }
    var item: ItemInfo? by remember { mutableStateOf(null) }

    // 별점
    var reviewStar: Int by remember { mutableStateOf(0) }
    // 내용
    var reviewContent: String by remember { mutableStateOf("") }
    // 별점 확인 모달
    var starCheck: Boolean by remember { mutableStateOf(false) }
    // 로딩창
    var isLoading: Boolean by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        // 결제 정보 조회
        try {
            if(isCreate){
                paymentDetail = coroutineScope.async { api.getPaymentDetail(paymentDetailIdx) }.await().result
                item = paymentDetail!!.item
            } else {
                review = coroutineScope.async { api.getReview(reviewIdx) }.await().result
                item = review!!.item
                reviewStar = review!!.star
                reviewContent = review!!.content
            }
        } catch (e: Exception){
            println("리뷰 생성/수정 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 리뷰 저장
    fun reviewSave() {
        val reviewBody = mapOf(
            "star" to reviewStar,
            "content" to reviewContent
        )

        coroutineScope.launch{
            try {
                val resultCode: String =
                    if (isCreate){
                        // 리뷰 생성
                        api.createReview(userId, paymentDetailIdx, reviewBody).resultCode
                    } else {
                        // 리뷰 수정
                        api.updateReview(userId, reviewIdx, reviewBody).resultCode
                    }
                if(resultCode == "SUCCESS"){
                    // 이전 페이지로 돌아감
                    navController.popBackStack()
                }
            } catch (e: Exception){
                println("리뷰 생성 / 수정 에러---------------")
                e.printStackTrace()
                println("---------------------------------")
            }
        }
    }

    Column() {
        // 상단바
        topBar(navController, "리뷰 작성")
        if (item != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .border(
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke((1.8).dp, Main_gray)
                        )
                        .padding(15.dp)
                ) {
                    Column {
                        // 상품 정보
                        Row(
                            modifier = Modifier
                                .clickable { navController.navigate("item/${item!!.itemIdx}") }
                        ) {
                            AsyncImage(
                                model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item!!.thumbnail}",
                                contentDescription = "상품 썸네일",
                                modifier = Modifier.size(75.dp),
                                onSuccess = {isLoading = false}
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = item!!.itemName,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                                Text(
                                    text =
                                        if(isCreate){
                                            "주문일자: ${paymentDetail!!.createTime[0]}. ${paymentDetail!!.createTime[1]}. ${paymentDetail!!.createTime[2]}"
                                        } else {
                                            "작성일자: ${review!!.date.substringBefore("T")}"
                                        },
                                    color = Main_gray,
                                    modifier = Modifier.padding(top = 5.dp),
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }

                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, bottom = 15.dp)
                        )

                        // 별점
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)){
                            Icon(imageVector = Icons.Default.Done, contentDescription = "체크", modifier = Modifier.padding(horizontal = 5.dp))
                            Text("상품 만족도")
                        }
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                            listOf(1, 2, 3, 4, 5).forEach {
                                Icon(imageVector = Icons.Filled.Star,
                                    contentDescription = "별점",
                                    tint = if (it <= reviewStar) Vivid_yellow else Color.LightGray,
                                    modifier = Modifier.clickable { reviewStar = it } .size(50.dp)
                                )
                            }
                        }

                        // 내용 입력
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)){
                            Icon(imageVector = Icons.Default.Done, contentDescription = "체크", modifier = Modifier.padding(horizontal = 5.dp))
                            Text("리뷰 작성")
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(15.dp, 0.dp, 15.dp, 15.dp)
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                                .border(width = (1.8).dp, color = Color.Transparent),
                            value = reviewContent,
                            onValueChange = { reviewContent = it },
                        )

                        // 확인 및 취소 버튼
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp, vertical = 5.dp)
                        ) {
                            OutlinedButton(
                                modifier = Modifier.padding(5.dp).fillMaxHeight().weight(1f),
                                elevation = ButtonDefaults.elevation(2.dp),
                                border = BorderStroke(color = Main_blue, width = 2.dp),
                                onClick = { reviewSave() }
                            ) {
                                Text("확인", color = Main_gray)
                            }
                            OutlinedButton(
                                modifier = Modifier.padding(5.dp).fillMaxHeight().weight(1f),
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
    // 이미지 로딩 중일 때
    if(isLoading){
        loadingView()
    }
}
