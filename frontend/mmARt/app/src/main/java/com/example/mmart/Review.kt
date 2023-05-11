package com.example.mmart

import android.media.Image
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun Review(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    // 조회한 리뷰 리스트
    var reviews : List<ReviewDetail>? by remember { mutableStateOf(null) }
    // 삭제 모달
    var isDelete:Int? by remember { mutableStateOf(null) }
    // 다시 불러오기
    var reload: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(reload) {
        // 리뷰 조회
        try {
            reviews = coroutineScope.async { api.getUserReview(userId) }.await().result
        } catch (e: Exception){
            println("리뷰 조회 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 리뷰 삭제
    @VisibleForTesting
    fun reviewDelete(reviewIdx: Int) {
        // 리뷰 삭제
        try{
            coroutineScope.async { api.deleteReview(userId, reviewIdx) }
        } catch (e: Exception){
            println("유저 - 리뷰 삭제 에러---------------")
            e.printStackTrace()
            println("---------------------------------")
        }

        // 모달 끄기
        isDelete = null

        // 리로드
        reload = !reload
    }
    
    Column() {
        // 상단바
        topBar(navController, "리뷰 내역 조회")

        if(reviews != null){
            // 작성한 리뷰가 없을 때
            if(reviews!!.isEmpty()){
                Text("작성한 리뷰가 없습니다.")

            // 작성한 리뷰가 있을 때
            } else {
                LazyColumn(){
                    items(reviews!!){
                        review ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
                        ){

                            // 구매 상품 정보
                            Row(
                                modifier = Modifier.clickable { navController.navigate("item/${review.item.itemIdx}") }
                            ) {
                                AsyncImage(model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${review.item.thumbnail}", contentDescription = "상품 썸네일")
                                Text(review.item.itemName)
                                Text("${review.paymentDetail.quantity}개 ${review.paymentDetail.totalPrice}")
                            }

                            // 별점
                            Row(){
                                repeat(review.star){
                                    Icon(Icons.Filled.Star,"별점", tint = Color.Yellow, )
                                }
                                repeat(5-review.star){
                                    Icon(Icons.Outlined.Star,"5-별점", tint = Color.LightGray)
                                }
                            }

                            // 리뷰 내용
                            Text(review.content)

                            // 수정, 삭제 버튼
                            Row(){
                                Button(onClick = {navController.navigate("reviewUpdate/${review.reviewIdx}")}) {
                                    Text("수정하기")
                                }
                                Button(onClick = { isDelete = review.reviewIdx }) {
                                    Text("삭제하기")
                                }
                            }
                        }

                        // 삭제 확인 다이얼로그
                        if(isDelete == review.reviewIdx){
                            AlertDialog(
                                onDismissRequest = { isDelete = null },
                                title = { Text("삭제 확인") },
                                text = { Text("해당 리뷰를 삭제하시겠습니까?") },
                                // 삭제 확인 버튼
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            reviewDelete(review.reviewIdx)
                                        }
                                    ) {
                                        Text("삭제")
                                    }
                                },
                                // 취소 버튼
                                confirmButton = {
                                    Button(onClick = { isDelete = null }) {
                                        Text("취소")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}