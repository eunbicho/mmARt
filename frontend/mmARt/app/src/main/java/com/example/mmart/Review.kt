package com.example.mmart

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun Review(navController: NavController) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // 조회한 리뷰 리스트
    var reviews: List<ReviewDetail>? by remember { mutableStateOf(null) }
    // 삭제 모달
    var isDelete: Int? by remember { mutableStateOf(null) }
    // 다시 불러오기
    var reload: Boolean by remember { mutableStateOf(false) }
    // 정렬
    var sort: Int by remember { mutableStateOf(0) }
    // 정렬 리스트
    val sortList = listOf("최신순", "별점 높은 순", "별점 낮은 순")
    // 정렬창 on
    var sortOpen: Boolean by remember { mutableStateOf(false) }
    // 전체 보기 / 일부 보기
    var expanded by remember { mutableStateOf(false) }
    // 로딩창
    var isLoading: Boolean by remember { mutableStateOf(true) }

    LaunchedEffect(reload) {
        // 리뷰 조회
        try {
            reviews = coroutineScope.async { api.getUserReview(userId) }.await().result
            println(reviews.toString())
        } catch (e: Exception) {
            println("리뷰 조회 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 아이템 리스트
    val reviewList =
        when(sort){
            0 -> reviews
            1 -> reviews!!.sortedByDescending { it.star }
            2 -> reviews!!.sortedBy { it.star }
            else -> reviews
        }

    // 리뷰 삭제
    fun reviewDelete(reviewIdx: Int) {
        // 리뷰 삭제
        coroutineScope.launch{
            try {
                val resultCode = api.deleteReview(userId, reviewIdx).resultCode
                if(resultCode == "SUCCESS"){
                    // 리로드
                    reload = !reload
                }
            } catch (e: Exception){
                println("리뷰 생성 / 수정 에러---------------")
                e.printStackTrace()
                println("---------------------------------")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 상단바
        topBar(navController, "리뷰 내역")

        if (reviews != null) {
            // 작성한 리뷰가 없을 때
            if (reviews!!.isEmpty()) {
                isLoading = false
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    blankView("작성한 리뷰가 없습니다.")
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(20.dp)
                                .clickable { navController.navigate("main") }
                        ) {
                            Image(painter = painterResource(R.drawable.main), contentDescription = "홈으로", Modifier.size(80.dp))
                            Text("홈으로", Modifier.padding(10.dp))
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(20.dp)
                                .clickable { navController.navigate("payment") }
                        ) {
                            Image(painter = painterResource(R.drawable.payment), contentDescription = "결제내역으로", Modifier.size(80.dp))
                            Text("결제내역으로", Modifier.padding(10.dp))
                        }
                    }
                }

            // 작성한 리뷰가 있을 때
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(state = listState, contentPadding = PaddingValues(bottom=100.dp)) {
                        // 정렬
                        item(){
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                                .padding(20.dp, 10.dp, 20.dp, 0.dp)){
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { sortOpen = true }){
                                    Text(sortList[sort])
                                    Icon(imageVector = Icons.Default.Menu, contentDescription = "정렬", modifier = Modifier.padding(start=10.dp))
                                }
                                DropdownMenu(expanded = sortOpen, onDismissRequest = { sortOpen = false }) {
                                    sortList.forEachIndexed { index, it ->
                                        DropdownMenuItem(
                                            onClick = {
                                                sort = index
                                                sortOpen = false
                                            }
                                        ) {
                                            Text(it)
                                        }
                                    }
                                }
                            }
                        }
                        items(reviewList!!) { review ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .border(
                                        width = (1.8).dp,
                                        color = Main_gray,
                                        shape = RoundedCornerShape(11.dp)
                                    )
                            ) {
                                // 구매 상품 정보
                                Row(modifier = Modifier
                                    .clickable { navController.navigate("item/${review.item.itemIdx}") }
                                    .padding(horizontal = 20.dp, vertical = 15.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${review.item.thumbnail}",
                                        contentDescription = "상품 썸네일",
                                        modifier = Modifier.size(70.dp),
                                        onSuccess = {isLoading = false}
                                    )
                                    Column(
                                        Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 20.dp)
                                    ) {
                                        Text(
                                            review.item.itemName,
                                            Modifier.padding(bottom = 5.dp)
                                        )
                                        Text(
                                            text = "작성일자: ${review.date.substringBefore("T")}",
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
                                        .padding(15.dp)
                                )

                                // 별점
                                Row(
                                    modifier = Modifier.padding(start = 20.dp)
                                ) {
                                    repeat(review.star) {
                                        Icon(Icons.Filled.Star, "별점", tint = Vivid_yellow)
                                    }
                                    repeat(5 - review.star) {
                                        Icon(Icons.Outlined.Star, "5-별점", tint = Color.LightGray)
                                    }
                                }
                                // 리뷰 내용
                                Text(review.content,
                                    modifier = Modifier
                                        .padding(vertical = 10.dp, horizontal = 20.dp)
                                        .fillMaxWidth()
                                        .clickable { expanded = !expanded },
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = if (expanded) Int.MAX_VALUE else 3
                                )

                                // 수정, 삭제 버튼
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(top = 5.dp)
                                ) {
                                    OutlinedButton(
                                        modifier = Modifier.padding(start = 5.dp, end = 10.dp, bottom = 10.dp),
                                        border = BorderStroke(color = Vivid_blue, width = 2.dp),
                                        onClick = { navController.navigate("reviewSave/0/${review.reviewIdx}") },
                                        elevation = ButtonDefaults.elevation(2.dp)
                                    ) {
                                        Text("수정하기", color = Main_gray)

                                    }
                                    OutlinedButton(
                                        modifier = Modifier.padding(start = 5.dp, end = 15.dp, bottom = 10.dp),
                                        border = BorderStroke(color = Vivid_yellow, width = 2.dp),
                                        onClick = { isDelete = review.reviewIdx },
                                        elevation = ButtonDefaults.elevation(2.dp)
                                    ) {
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
                                        OutlinedButton(
                                            border = BorderStroke(color = Main_blue, width = 2.dp),
                                            elevation = ButtonDefaults.elevation(2.dp),
                                            onClick = {
                                                reviewDelete(review.reviewIdx)
                                                reload = !reload
                                            })
                                        {
                                            Text("삭제", color = Main_gray)
                                        }
                                    },
                                    // 취소 버튼
                                    confirmButton = {
                                        OutlinedButton(
                                            border = BorderStroke(color = Main_yellow, width = 2.dp),
                                            elevation = ButtonDefaults.elevation(2.dp),
                                            onClick = { isDelete = null }) {
                                            Text("취소", color = Main_gray)
                                        }
                                    })
                            }
                        }
                    }
                    // 하단 버튼
                    floatingBtn(listState)
                }
            }
        }
    }
    // 이미지 로딩 중일 때
    if(isLoading){
        loadingView()
    }
}