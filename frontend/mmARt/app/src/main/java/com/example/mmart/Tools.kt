package com.example.mmart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 검색바
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun searchBar(navController: NavController){
    var searchWord by remember { mutableStateOf("") }

    fun search(){
        navController.navigate("search/$searchWord")
    }

    fun barcodeSearch(){
        navController.navigate("barcodeScan")
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
                ambientColor = Color.Black,
                clip = true
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Main_yellow,
            textColor = Dark_gray,
        ),
        singleLine = true,
        textStyle = TextStyle(fontFamily = mainFont, fontSize = 15.sp),

        // textfield 우측에 아이콘 추가
        trailingIcon = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.barcode),
                    contentDescription = "바코드 검색",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { barcodeSearch() }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            search()
                        }
                )

            }
        },
        // 키보드 모양 바꾸기
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // 엔터(키보드에서 search아이콘) 클릭 시 실행
        keyboardActions = KeyboardActions(onSearch = {
            search()
            keyboardController?.hide()
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
            .padding(15.dp),
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
        modifier = Modifier
            .padding(20.dp, 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }},
            backgroundColor = Light_gray,
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
            .padding(20.dp, 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(index = 0)
                }},
            backgroundColor = Light_gray,
            modifier = Modifier.sizeIn(60.dp, 60.dp, 80.dp, 80.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.top),
                contentDescription = "TOP",
            )
        }

        FloatingActionButton(
            onClick = secondEvent,
            backgroundColor = Light_gray,
            modifier = Modifier.sizeIn(60.dp, 60.dp, 80.dp, 80.dp),
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
fun loadingView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun ToolPreview(){
    val navController = rememberNavController()
    topBar(navController = navController, "상세보기")
}
