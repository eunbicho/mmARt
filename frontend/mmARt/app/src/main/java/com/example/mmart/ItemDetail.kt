package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun ItemDetail(navController: NavController, itemId: Int?){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    // 아이템 정보
    var item: ItemInfo? by remember { mutableStateOf(null) }
    // 수량
    var quantity: Int by remember { mutableStateOf(1) }
    // 리뷰 리스트
    var reviews: List<ReviewDetail>? by remember { mutableStateOf(null) }
    // 삭제 모달
    var isDelete:Int? by remember { mutableStateOf(null) }
    // 다시 불러오기
    var reload: Boolean by remember { mutableStateOf(false) }

    // 상세 조회
    LaunchedEffect(reload) {
        try {
            item = coroutineScope.async { api.getItemInfo(itemId!!) }.await().result
        } catch (e: Exception){
            println("상픔 상세 에러---------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
        try {
            reviews = coroutineScope.async { api.getItemReview(itemId!!) }.await().result
        } catch (e: Exception){
            println("상픔 상세 리뷰 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    fun addGetCart(){
        var body = mapOf(
            "itemIdx" to item!!.itemIdx,
            "quantity" to quantity,
            "userIdx" to userId
        )
        try {
            coroutineScope.async { api.addGetCart(body) }
        } catch (e: Exception){
            println("장볼구니 추가 에러------------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    // 리뷰 삭제
    fun reviewDelete(reviewIdx: Int) {
        // 리뷰 삭제
        try{
            coroutineScope.async { api.deleteReview(userId, reviewIdx) }
        } catch (e: Exception){
            println("아이템 - 리뷰 삭제 에러-------------")
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
        topBar(navController = navController, "상품 상세")
        Column(
            modifier = Modifier.verticalScroll(state = rememberScrollState())
        ){
            // result가 null이 아닐 경우만
            if (item != null) {
                // 상품 이미지
                AsyncImage(
                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item!!.thumbnail.replace("_thumb", "")}",
                    contentDescription = "상품 이미지")

                Row(){
                    Text(item!!.itemName)

                    // 가격
                    if(item!!.isCoupon){ // 쿠폰 있을 경우
                        Column() {
                            Text("${item!!.price}", textDecoration = TextDecoration.LineThrough, color = Color.LightGray, fontWeight = FontWeight.Light)
                            Text("${item!!.couponPrice}원")
                        }
                    } else { // 쿠폰 없을 경우
                        Text("${item!!.price}원")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 수량 조절
                    IconButton(
                        onClick = { quantity-- },
                        enabled = 1 < quantity
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "수량 감소")
                    }
                    Text(quantity.toString(), modifier = Modifier.clickable {  })
//                    OutlinedTextField(
//                        value = quantity.toString(),
//                        onValueChange = { input ->
//                            println(input)
//                            input.toIntOrNull()?.let { value ->
//                                if (value in 1..item!!.inventory) {
//                                    quantity = value
//                                } else {
//                                    println("nope")
//                                }
//                            } ?: run {
//                                quantity = 1
//                            }
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        textStyle = TextStyle(textAlign = TextAlign.Start),
//                        modifier = Modifier.size(50.dp),
//                        singleLine = true
//                    )
                    IconButton(
                        onClick = { quantity++ },
                        enabled = quantity < item!!.inventory
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "수량 증가")
                    }

                    // 장바구니 추가 버튼
                    Button(onClick = { addGetCart() }) {
                        Text("장볼구니")
                    }
                }

                // 상품 상제 이미지
                AsyncImage(
                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item!!.content}",
                    contentDescription = "상품 상세 정보",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
            )

            // 리뷰 부분
            Text(text = "리뷰")

            if (reviews != null && reviews!!.isNotEmpty()){
                reviews!!.forEach { review ->
                    Row(){
                        repeat(review.star){
                            Icon(Icons.Filled.Star,"별점", tint = Color.Yellow, )
                        }
                        repeat(5-review.star){
                            Icon(Icons.Filled.Star,"5-별점", tint = Color.LightGray)
                        }
                    }
                    Text(review.content)

                    // 내가 작성한 리뷰인 경우, 수정 및 삭제 가능
                    if(review.user.userIdx == userId) {
                        Row(){
                            Button(onClick = { navController.navigate("reviewUpdate/${review.reviewIdx}") }) {
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

            } else {
                 Text("작성된 리뷰가 없습니다.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview(){
    val navController = rememberNavController()
    ItemDetail(navController, 1)
}

