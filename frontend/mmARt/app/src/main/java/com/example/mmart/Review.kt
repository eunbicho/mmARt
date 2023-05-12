package com.example.mmart

import android.text.style.BackgroundColorSpan
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Main_yellow
import kotlinx.coroutines.async

@Composable
fun Review(navController: NavController) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    // 조회한 리뷰 리스트
    var reviews: List<ReviewDetail>? by remember { mutableStateOf(null) }
    // 삭제 모달
    var isDelete: Int? by remember { mutableStateOf(null) }
    // 다시 불러오기
    var reload: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(reload) {
        // 리뷰 조회
        try {
            reviews = coroutineScope.async { api.getUserReview(userId) }.await().result
        } catch (e: Exception) {
            println("리뷰 조회 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 리뷰 삭제
    @VisibleForTesting
    fun reviewDelete(reviewIdx: Int) {
        // 리뷰 삭제
        try {
            coroutineScope.async { api.deleteReview(userId, reviewIdx) }
        } catch (e: Exception) {
            println("유저 - 리뷰 삭제 에러---------------")
            e.printStackTrace()
            println("---------------------------------")
        }

        // 모달 끄기
        isDelete = null

        // 리로드
        reload = !reload
    }

    Column(
        modifier = Modifier.padding(bottom = 23.dp)
    ) {
        // 상단바
        topBar(navController, "리뷰 내역 조회")

        if (reviews != null) {
            // 작성한 리뷰가 없을 때
            if (reviews!!.isEmpty()) {
                Text("작성한 리뷰가 없습니다.")

                // 작성한 리뷰가 있을 때
            } else {
                LazyColumn {
                    items(reviews!!) { review ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 23.dp, start = 23.dp, end = 23.dp)
                                .border(width = (1.8).dp, color = Main_gray, shape = RoundedCornerShape(11.dp))
                        ) {

                            // 구매 상품 정보
                            Row(modifier = Modifier.clickable { navController.navigate("item/${review.item.itemIdx}") }
                                .padding(start = 15.dp, top = 15.dp)) {
                                AsyncImage(
                                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${review.item.thumbnail}",
                                    contentDescription = "상품 썸네일",
                                    modifier = Modifier.size(75.dp)
                                )
                                Column(
                                    Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)
                                ) {
                                    Text(
                                        review.item.itemName,
                                        Modifier.padding(bottom = 5.dp)
                                    )
                                    Text(
                                        review.date.split("T")[0],
                                        Modifier.padding(top = 5.dp),
                                        color = Main_gray
                                    )
                                }

                            }

                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            )

                            // 별점
                            Row(
                                modifier = Modifier.padding(start = 15.dp)
                            ) {
                                repeat(review.star) {
                                    Icon(Icons.Filled.Star, "별점", tint = Color.Yellow)
                                }
                                repeat(5 - review.star) {
                                    Icon(Icons.Outlined.Star, "5-별점", tint = Color.LightGray)
                                }
                            }
                            // 리뷰 내용
                            Text(
                                modifier = Modifier.padding(start = 15.dp, top = 15.dp), text = review.content
                            )

                            // 수정, 삭제 버튼
                            Row(
                                modifier = Modifier.align(Alignment.End).padding(top = 5.dp)
                            ) {
                                OutlinedButton(
                                    modifier = Modifier.padding(start = 5.dp, end = 10.dp, bottom = 10.dp),
                                    border = BorderStroke(color = Main_blue, width = 2.dp),
                                    onClick = { navController.navigate("reviewUpdate/${review.reviewIdx}") },
                                    elevation =  ButtonDefaults.elevation(2.dp)
                                ){
                                    Text("수정하기", color = Main_gray)

                                }
                                OutlinedButton(
                                    modifier = Modifier.padding(start = 5.dp, end = 15.dp, bottom = 10.dp),
                                    border = BorderStroke(color = Main_yellow, width = 2.dp),
                                    onClick = { navController.navigate("reviewUpdate/${review.reviewIdx}") },
                                    elevation =  ButtonDefaults.elevation(2.dp)
                                ){
                                    Text("삭제하기", color = Main_gray)
                                }
                            }
                        }
                        // 삭제 확인 다이얼로그
                        if (isDelete == review.reviewIdx) {
                            AlertDialog(onDismissRequest = { isDelete = null },
                                title = { Text("삭제 확인") },
                                text = { Text("해당 리뷰를 삭제하시겠습니까?") },
                                // 삭제 확인 버튼
                                dismissButton = {
                                    OutlinedButton(onClick = {
                                        reviewDelete(review.reviewIdx)
                                    }) {
                                        Text("삭제")
                                    }
                                },
                                // 취소 버튼
                                confirmButton = {
                                    OutlinedButton(onClick = { isDelete = null }) {
                                        Text("취소")
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}