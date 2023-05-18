package com.example.mmart

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.launch

// 검색바
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun searchBar(navController: NavController){
    var searchWord by remember { mutableStateOf("") }

    fun search(){
        navController.navigate("search/$searchWord")
    }

    // 키보드 조정
    val keyboardController = LocalSoftwareKeyboardController.current

    // 검색창
    OutlinedTextField(
        value = searchWord,
        onValueChange = { searchWord = it },
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(20.dp, 0.dp)
            .border(color = Dark_gray, width = 1.5.dp, shape = CircleShape)
            .shadow(
                shape = CircleShape,
                elevation = 5.dp,
                ambientColor = Dark_gray,
                clip = true
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Main_yellow,
            textColor = Dark_gray,
            cursorColor = Dark_gray,
            focusedBorderColor = Dark_gray,
        ),
        singleLine = true,
        textStyle = TextStyle(fontFamily = mainFont, fontSize = 15.sp),
        // textfield 우측에 아이콘 추가
        trailingIcon = {
            Image(
                painter = painterResource(R.drawable.search),
                contentDescription = "검색",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        if (searchWord
                                .trim()
                                .isNotEmpty()
                        ) search()
                    }
            )
        },
        // 키보드 모양 바꾸기
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // 엔터(키보드에서 search아이콘) 클릭 시 실행
        keyboardActions = KeyboardActions(onSearch = {
            if (searchWord.trim().isNotEmpty()) {
                search()
                keyboardController?.hide()
            }
        })
    )
}

@Composable
fun topBar(navController: NavController, title: String){
 Column() {
    Row (
        modifier = Modifier
            .background(color = Color(0XFFF5F5F5))
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ){
        Image(painter = painterResource(R.drawable.back), contentDescription = "뒤로 가기", modifier = Modifier
            .clickable {
                navController.previousBackStackEntry?.let {
                    navController.popBackStack(it.destination.id, false)
                }
            }
        )
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Light)
        Image(painter = painterResource(R.drawable.home), contentDescription = "홈으로", modifier = Modifier
            .clickable { navController.popBackStack(navController.graph.startDestinationId, false) }
        )
    }
    Divider(startIndent = 0.dp, thickness = 1.dp, color = Main_gray)
 }
}

// 하단 버튼바 1개짜리
@Composable
fun floatingBtn(
    listState: LazyListState,
){
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }},
            modifier = Modifier.sizeIn(60.dp, 60.dp, 80.dp, 80.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.top),
                contentDescription = "TOP",
            )
        }

    }
}

// 하단 버튼바 2개짜리
@Composable
fun floatingBtns(
    listState: LazyListState,
    secondBtn: Int,
    secondBtnName: String,
    secondEvent: () -> Unit
){
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }},
            modifier = Modifier
                .sizeIn(60.dp, 60.dp, 80.dp, 80.dp)
                .clip(CircleShape),
            backgroundColor = Color.White,
        ) {
            Image(
                painter = painterResource(R.drawable.top),
                contentDescription = "TOP",
            )
        }

        FloatingActionButton(
            onClick = secondEvent,
            modifier = Modifier
                .sizeIn(60.dp, 60.dp, 80.dp, 80.dp)
                .clip(CircleShape),
            backgroundColor = Color.White,
        ) {
            Image(
                painter = painterResource(secondBtn),
                contentDescription = secondBtnName,
            )
        }
    }
}

@Composable
fun blankView(msg: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(msg)
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.loading).apply(block = {
                size(240)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun loadingView(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Vivid_blue),
        contentAlignment = Alignment.Center,
    ) {
        GifImage()
    }
}

@Composable
fun items(navController:NavController, items: List<ItemInfo>, loading: (Boolean) -> Unit){
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyGridState()

    var sort: String by remember { mutableStateOf("추천순") }
    var expanded: Boolean by remember { mutableStateOf(false) }

    // 아이템 리스트
    val itemList =
        when(sort){
            "추천순" -> items
            "낮은 가격순" -> items.sortedBy { it.couponPrice }
            "높은 가격순" -> items.sortedByDescending { it.couponPrice }
            else -> items
        }

    Box(modifier = Modifier
        .fillMaxSize()) {
        LazyVerticalGrid(GridCells.Fixed(2), state=listState, contentPadding = PaddingValues(bottom=90.dp)) {
            // 정렬
            item(span = { GridItemSpan(2) }){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
                    .padding(20.dp, 10.dp, 20.dp, 0.dp)){
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { expanded = true }){
                        Text(sort)
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "정렬", modifier = Modifier.padding(start=10.dp))
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("추천순", "낮은 가격순", "높은 가격순").forEach {
                            DropdownMenuItem(
                                onClick = {
                                    sort = it
                                    expanded = false
                                }
                            ) {
                                Text(it)
                            }
                        }
                    }
                }
            }
            items(itemList) { item ->
                // 수량
                var quantity: Int by remember { mutableStateOf(1) }
                // 장볼구니 모달
                var goGetCart: Boolean by remember { mutableStateOf(false) }
                // 장볼구니 추가
                fun addGetCart(){
                    val body = mapOf(
                        "itemIdx" to item.itemIdx,
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


                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .clickable { navController.navigate("item/${item.itemIdx}") }
                    ){
                        AsyncImage(
                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail.replace("_thumb", "")}",
                            contentDescription = "상품 썸네일",
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f)
                                .padding(5.dp),
                            onSuccess = { loading(false) }
                        )

                        Text(text = item.itemName, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 20.sp)

                        // 가격
                        if(item.inventory>0){
                            if(item.isCoupon){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${item.couponPrice}원", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(5.dp))
                                    Text("${item.price}", textDecoration = TextDecoration.LineThrough, color = Color.LightGray, fontWeight = FontWeight.Light, fontSize = 15.sp)
                                }
                            } else{
                                Text(text = "${item.price}원", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(text = "품절", fontSize = 20.sp, color = Color.LightGray)
                        }

                    }
                    // 수량, 장볼구니 추가 버튼
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { quantity-- },
                            enabled = 1 < quantity
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "수량 감소")
                        }
                        Text(quantity.toString())
                        IconButton(
                            onClick = { quantity++ },
                            enabled = quantity < item.inventory
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "수량 증가")
                        }
                        if(item.inventory>0){
                            Image(
                            painter = painterResource(R.drawable.getcart),
                            contentDescription = "장볼구니 추가",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 20.dp)
                                .weight(1f)
                                .wrapContentWidth(align = Alignment.End)
                                .clickable { addGetCart() })
                        }else{
                            Image(
                                painter = painterResource(R.drawable.getcart_x),
                                contentDescription = "장볼구니 추가X",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 20.dp)
                                    .weight(1f)
                                    .wrapContentWidth(align = Alignment.End))
                            }

                    }
                }

                // 장볼구니 모달
                if (goGetCart){
                    Dialog(
                        onDismissRequest = { goGetCart = false }
                    ) {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(40.dp)
                                )
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
            }
        }

        // 하단 버튼
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight()
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }},
                backgroundColor = Light_gray,
                modifier = Modifier
                    .sizeIn(60.dp, 60.dp, 80.dp, 80.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.top),
                    contentDescription = "TOP",
                )
            }
        }
    }

}






@Preview(showBackground = true)
@Composable
fun toolPreview(){
}

