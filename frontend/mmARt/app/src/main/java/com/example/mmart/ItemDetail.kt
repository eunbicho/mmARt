package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import okhttp3.internal.wait
import androidx.lifecycle.lifecycleScope


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ItemDetail(navController: NavController, itemId: Int?, modifier: Modifier = Modifier){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    // 아이템 정보
    var item: ItemInfo? by remember { mutableStateOf(null) }
    // 수량
    var quantity: Int by remember { mutableStateOf(1) }
    // 리뷰 리스트
    var reviews: List<ReviewDetail>? by remember { mutableStateOf(null) }
    // 긍부정
    var pos: Int? by remember { mutableStateOf(null) }

    // bottomSheet 모달에서 사용
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    // 키보드 조정
    val keyboardController = LocalSoftwareKeyboardController.current
    // 포커스 조정
    val focusRequester = remember { FocusRequester() }
    // 수량 모달 안에서 input 된 값
    var numberInput: String by remember { mutableStateOf("") }
    // 잘못된 수량 입력 여부 (수량 확인 요청 모달)
    var wrongNumber: Boolean by remember { mutableStateOf(false) }
    // 장볼구니 모달
    var goGetCart: Boolean by remember { mutableStateOf(false) }
    // 삭제 모달
    var isDelete:Int? by remember { mutableStateOf(null) }
    // 다시 불러오기
    var reload: Boolean by remember { mutableStateOf(false) }
    // 로딩창
    var isLoading: Boolean by remember { mutableStateOf(true) }

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
            val response = coroutineScope.async { api.getItemReview(itemId!!) }.await().result
            reviews = response.reviewRes
            pos = response.pos
        } catch (e: Exception){
            println("상픔 상세 리뷰 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }

    }

    // 수량 확인
    fun numberCheck(){
        try {
            if(1 <= numberInput.toInt() && numberInput.toInt() <= item!!.inventory){
                quantity = numberInput.toInt()
                coroutineScope.launch { sheetState.hide() }
            } else {
                wrongNumber = true
                keyboardController?.show()
            }
        } catch(e:NumberFormatException) {
            wrongNumber = true
        }
    }

    // 장볼구니 추가
    fun addGetCart(){
        val body = mapOf(
            "itemIdx" to item!!.itemIdx,
            "quantity" to quantity,
            "userIdx" to userId
        )
        coroutineScope.launch{
            try {
               val resultCode = api.addGetCart(body).resultCode
                if(resultCode == "SUCCESS"){
                    goGetCart = true
                }
            } catch (e: Exception){
                println("장볼구니 추가 에러------------------")
                e.printStackTrace()
                println("---------------------------------")
                }
        }
    }

    // 리뷰 삭제
    fun reviewDelete(reviewIdx: Int) {
        // 리뷰 삭제
        coroutineScope.launch{
            try{
                val resultCode = api.deleteReview(userId, reviewIdx).resultCode
                if(resultCode == "SUCCESS"){
                    // 모달 끄기
                    isDelete = null
                    // 리로드
                    reload = !reload
                }
            } catch (e: Exception){
                println("아이템 - 리뷰 삭제 에러-------------")
                e.printStackTrace()
                println("---------------------------------")
            }
        }
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
                    contentDescription = "상품 이미지",
                    modifier = Modifier.padding(20.dp)
                )

                // 상품명, 매장 수량, 가격
                Column(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                ){
                    // 상품명
                    Text(item!!.itemName, fontSize = 25.sp, modifier = Modifier.padding(5.dp))

                    Row(verticalAlignment = Alignment.Bottom){
                        // 매장 수량
                        Text("매장 수량: ${item!!.inventory}", fontSize = 15.sp, textAlign = TextAlign.Start)
                        // 가격
                        if(item!!.isCoupon){ // 쿠폰 있을 경우
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${item!!.price}", textDecoration = TextDecoration.LineThrough, color = Color.LightGray, fontWeight = FontWeight.Light, fontSize = 20.sp)
                                Text("${item!!.couponPrice}원", fontSize = 25.sp, modifier = Modifier.padding(5.dp))
                            }
                        } else { // 쿠폰 없을 경우
                            Text("${item!!.price}원", fontSize = 25.sp, textAlign = TextAlign.End, modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp))
                        }
                    }
                }

                // 수량, 총 금액, 장볼구니 추가 버튼
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 10.dp)
                ) {
                    // 수량 조절
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.3f)
                    ) {
                        IconButton(
                            onClick = { quantity-- },
                            enabled = 1 < quantity
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "수량 감소")
                        }
                        Text(quantity.toString(), modifier = Modifier.clickable {
                            numberInput = ""
                            // 모달 열기
                            coroutineScope.launch {sheetState.show()}
                            // 열리면서 포커스 조정
                            focusRequester.requestFocus()
                        })
                        IconButton(
                            onClick = { quantity++ },
                            enabled = quantity < item!!.inventory
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "수량 증가")
                        }
                    }

                    // 총 금액, 장볼구니 추가 버튼
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        // 수량 * 가격
                        if(item!!.isCoupon){ // 쿠폰 있을 경우
                            Text("${item!!.couponPrice * quantity}원", modifier= Modifier.padding(10.dp))
                        } else { // 쿠폰 없을 경우
                            Text("${item!!.price * quantity}원", modifier= Modifier.padding(10.dp))
                        }

                        // 장바구니 추가 버튼
                        Button(
                            onClick = {  addGetCart() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Vivid_blue),
                        ) {
                            Text("장볼구니", color = Color.White)
                        }
                    }

                }

                // 상품 상제 이미지
                AsyncImage(
                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item!!.content}",
                    contentDescription = "상품 상세 정보",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    onSuccess = {isLoading = false}
                )
            }

            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            // 리뷰 부분
            Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "리뷰", fontSize = 20.sp)
                if(pos!=-1){
                    Text("${pos}%의 사용자가 긍정적인 평가를 하였습니다")
                }
            }

            if (reviews != null && reviews!!.isNotEmpty()){
                reviews!!.forEach { review ->
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            repeat(review.star){
                                Icon(Icons.Filled.Star,"별점", tint = Vivid_yellow, )
                            }
                            repeat(5-review.star){
                                Icon(Icons.Filled.Star,"5-별점", tint = Color.LightGray)
                            }
                            Text(review.user.name, fontSize = 13.sp, textAlign = TextAlign.End, color = Main_gray,modifier = Modifier.weight(1f))
                        }

                        // 전체 보기 / 일부 보기
                        var expanded by remember { mutableStateOf(false) }
                        Text(review.content, modifier = Modifier
                            .padding(vertical = 5.dp)
                            .clickable { expanded = !expanded }, overflow = TextOverflow.Ellipsis, maxLines = if (expanded) Int.MAX_VALUE else 3)

                        // 내가 작성한 리뷰인 경우, 수정 및 삭제 가능
                        if(review.user.userIdx == userId) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .height(ButtonDefaults.MinHeight),
                            ) {
                                OutlinedButton(
                                    modifier = Modifier.padding(end = 5.dp),
                                    border = BorderStroke(color = Main_blue, width = 2.dp),
                                    onClick = { navController.navigate("reviewUpdate/${review.reviewIdx}") },
                                    elevation = ButtonDefaults.elevation(2.dp)
                                ) {
                                    Text("수정하기", color = Main_gray)

                                }
                                OutlinedButton(
                                    modifier = Modifier.padding(start = 5.dp),
                                    border = BorderStroke(color = Main_yellow, width = 2.dp),
                                    onClick = { isDelete = review.reviewIdx },
                                    elevation = ButtonDefaults.elevation(2.dp)
                                ) {
                                    Text("삭제하기", color = Main_gray)
                                }
                            }
                        }
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    }

                    // 삭제 확인 다이얼로그
                    if(isDelete == review.reviewIdx){
                        AlertDialog(
                            onDismissRequest = { isDelete = null },
                            title = { Text("삭제 확인") },
                            text = { Text("해당 리뷰를 삭제하시겠습니까?") },
                            // 삭제 확인 버튼
                            dismissButton = {
                                OutlinedButton(
                                    onClick = {
                                        reviewDelete(review.reviewIdx)
                                    },
                                    elevation = ButtonDefaults.elevation(1.dp)
                                ) {
                                    Text("삭제", color = Color.Black)
                                }
                            },
                            // 취소 버튼
                            confirmButton = {
                                OutlinedButton(
                                    onClick = { isDelete = null },
                                    elevation = ButtonDefaults.elevation(1.dp)
                                ) {
                                    Text("취소", color = Color.Black)
                                }
                            },
                            modifier = Modifier.border(1.dp, Main_gray)
                        )
                    }

                }

            } else {
                 Text("작성된 리뷰가 없습니다.", modifier = Modifier.padding(20.dp))
            }
        }
    }

    // 모달 보이냐 안 보이냐에 따라 키보드 조정
    if (sheetState.isVisible) {
        keyboardController?.show()
    } else {
        keyboardController?.hide()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                TextField(
                    value = numberInput,
                    onValueChange = { numberInput=it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            numberCheck()
                        }
                    ),
                    modifier = Modifier
                        // 포커스 (바로 키보드 열기위해)
                        .focusRequester(focusRequester)
                )
                OutlinedButton(
                    onClick = {numberCheck()},
                    elevation = ButtonDefaults.elevation(1.dp),
                ) {
                    Text("확인", color = Color.Black)
                }
            }

            // 수량 키보드 입력 확인
            if(wrongNumber){
                AlertDialog(
                    onDismissRequest = {
                        wrongNumber = false
                        keyboardController?.show()
                    },
                    text = { Text("수량을 확인하여 주십시오") },
                    confirmButton = {
                        OutlinedButton(
                            onClick = {
                            wrongNumber = false
                            keyboardController?.show()
                            },
                            elevation = ButtonDefaults.elevation(1.dp)
                        ) {
                            Text("확인", color = Color.Black)
                        }
                    }
                )
            }
        },
        content = {}
    )

    // 장볼구니 모달
    if(goGetCart){
        Dialog(
            onDismissRequest = { goGetCart = false }
        ) {
            Box(
                modifier = Modifier
                    .border(width = 3.dp, color = Color.Black, shape = RoundedCornerShape(40.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(40.dp))
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("상품이 장볼구니에\n추가되었습니다", modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp), fontSize = 20.sp, textAlign = TextAlign.Center)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { goGetCart = false }
                        ) {
                            Image(painter = painterResource(R.drawable.previous), contentDescription = "이전으로", Modifier.size(70.dp))
                            Text("이전으로", Modifier.padding(5.dp))
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { navController.navigate("getCart") }
                        ) {
                            Image(painter = painterResource(R.drawable.getcart), contentDescription = "장볼구니로", Modifier.size(70.dp))
                            Text("장볼구니로", Modifier.padding(5.dp))
                        }
                    }
                }
            }
        }
    }

    // 이미지 로딩 중일 때
    if(isLoading){
        loadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview(){
    var isWrong = true
    AlertDialog(
        onDismissRequest = { isWrong = false },
        text = { Text("아이디, 비밀번호 확인 후\n\n다시 로그인 해주세요", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 18.sp) },
        confirmButton = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedButton(
                    onClick = { isWrong = false },
                    elevation = ButtonDefaults.elevation(1.dp)
                ) {
                    Text("확인", color = Color.Black)
                }
            }
        },
        modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    )
}

