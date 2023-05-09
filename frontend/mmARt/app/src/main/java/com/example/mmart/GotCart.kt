package com.example.mmart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun GotCart(navController: NavController){

    val userId = 1
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var resultCart: CartContent? by remember { mutableStateOf(null) }
    var resultCode: String? by remember { mutableStateOf(null) }
    var resultUser: UserInfo? by remember { mutableStateOf(null) }

    var showQrcode by remember { mutableStateOf(false)}

    // 한 번만 실행
    LaunchedEffect(true) {
        val cartRes = coroutineScope.async { api.gotCartsRead(userId) }.await()
        resultCart = cartRes.result
        resultCode = cartRes.resultCode
        val userRes = coroutineScope.async { api.getUser(userId) }.await()
        resultUser = userRes.result
    }


    Column() {
        topBar(navController = navController, "장봤구니")

        // result가 null이 아닐 경우만
        if(resultCart != null){
            if (resultCode == "SUCCESS") {
                if (resultCart!!.itemList.isNotEmpty()) {
                    LazyColumn(
                    ) {
                        items(resultCart!!.itemList) {
                                item ->
                            Surface(color = MaterialTheme.colors.primary) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { navController.navigate("item/${item.itemIdx}") },
                                        model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                        contentDescription = item.itemName,
                                    )
                                    Column() {
                                        Text(
                                            text = "${item.itemName}",
                                            modifier = Modifier
                                                .clickable { navController.navigate("item/${item.itemIdx}") },
                                        )
                                        Text(text = "${item.price}원")
                                    }
                                    Text(text = "수량 : ${item.quantity}")
                                    Button(onClick = { deleteGotCart(item.itemIdx) }) {
                                        Text(text = "X")
                                    }
                                }
                            }
                        }
                    }
                    Text(text = "합계 : ${resultCart!!.total}원")
                } else {
                    Text("장봤구니가 비어있습니다.")
                }
            } else {
                Text("장봤구니를 찾을 수 없습니다.")
            }
        }
        Row() {

            Button(onClick = { navController.navigate("main") }) {
                Text(text = "메인으로")
            }

            Button(onClick = { showQrcode = true }) {
                Text(text = "결제하기")
            }
        }
    }

    if (showQrcode) {
        Dialog(
            onDismissRequest = { showQrcode = false },
            content = {
                Card (
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 8.dp
                ){
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("아래 QR코드를 키오스크에 인식해주세요.")
                        AsyncImage(
                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${resultUser?.qrcode}",
                            modifier = Modifier.fillMaxWidth(),
                            contentDescription = resultUser?.name
                        )
                        Button(onClick = {
                            showQrcode = false
                        }) {
                            Text("닫기")
                        }
                    }
                }
            }
        )
    }
}

fun deleteGotCart(itemIdx: Int) {
    println(itemIdx)
}
