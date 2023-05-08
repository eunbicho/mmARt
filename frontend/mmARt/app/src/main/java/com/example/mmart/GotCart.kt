package com.example.mmart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun GotCart(navController: NavController, userId: Int?){
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var resultCart: CartContent? by remember { mutableStateOf(null) }
    var resultCode: String? by remember { mutableStateOf(null) }
    var resultUser: UserInfo? by remember { mutableStateOf(null) }

    var showQrcode by remember { mutableStateOf(false)}

    // 한 번만 실행
    LaunchedEffect(true) {
        val cartRes = coroutineScope.async { api.gotCartsRead(userId!!) }.await()
        resultCart = cartRes.result
        resultCode = cartRes.resultCode
        val userRes = coroutineScope.async { api.getUser(userId!!) }.await()
        resultUser = userRes.result
    }

    Column() {
        Row() {
            Text(text = "장봤구니")
        }
        // result가 null이 아닐 경우만
        if(resultCart != null){
            if (resultCode == "SUCCESS") {
                LazyColumn(
                ){
                    items(resultCart!!.itemList){
                            item ->
                        Surface(color = MaterialTheme.colors.primary) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                AsyncImage(
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                                    model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                    contentDescription = item.itemName
                                )
                                Text(text = item.itemName)
                                Text(text = "수량 : ${item.quantity}")
                                Text(text = "${item.price}원")
                                Button(onClick = { deleteGotCart(item.itemIdx) }) {
                                    Text(text = "X")
                                }
                            }
                        }
                    }
                }
                    Text(text = "합계 : ${resultCart!!.total}원")
            } else {
                Text(resultCart!!.message)
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
                Surface (
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
    APIS.create()
}
