package com.example.mmart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Main_yellow
import kotlinx.coroutines.async

@Composable
fun Update(navController: NavController, reviewIdx: Int) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    // 리뷰 정보
    var review: ReviewDetail? by remember { mutableStateOf(null) }
    // 수정 시 별점
    var reviewStar: Int by remember { mutableStateOf(1) }
    // 수정 시 내용
    var reviewContent: String by remember { mutableStateOf("") }

    var reload: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(reload) {
        // 리뷰 조회
        try {
            review = coroutineScope.async { api.getReview(reviewIdx) }.await().result
            reviewStar = review!!.star
            reviewContent = review!!.content
        } catch (e: Exception) {
            println("리뷰 수정 조회 에러-----------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    fun reviewUpdate() {
        val reviewBody = mapOf(
            "star" to reviewStar, "content" to reviewContent
        )

        // 리뷰 수정
        try {
            coroutineScope.async { api.updateReview(userId, reviewIdx, reviewBody) }
        } catch (e: Exception) {
            println("리뷰 수정 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }

        // 이전 페이지로 돌아감
        reload = !reload
        navController.popBackStack()
    }

    Column {
        // 상단바
        topBar(navController, "리뷰 수정")

        if (review != null) {
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
                                .clickable { navController.navigate("item/${review!!.item.itemIdx}") }
                        ) {
                            AsyncImage(
                                model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${review!!.item.thumbnail}",
                                contentDescription = "상품 썸네일",
                                modifier = Modifier.size(75.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = review!!.item.itemName,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                                Text(
                                    text = review!!.date.split("T")[0],
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
                                onClick = { reviewUpdate() }
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
    }
}
