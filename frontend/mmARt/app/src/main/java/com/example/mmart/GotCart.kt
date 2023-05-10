package com.example.mmart

import android.text.style.LineHeightSpan
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage

import kotlinx.coroutines.*
import java.text.DecimalFormat

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

    Column {
        topBar(navController = navController, "장봤구니")

        // result가 null이 아닐 경우만
        if(resultCart != null){
            if (resultCode == "SUCCESS") {
                if (resultCart!!.itemList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .padding(20.dp)
                    ) {
                        items(resultCart!!.itemList) {
                                item ->
                            var quantity by remember { mutableStateOf(TextFieldValue("${item.quantity}")) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .border(
                                        color = Color.DarkGray,
                                        width = 1.5.dp,
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                backgroundColor = Color.hsl(194f,0.5f,0.9f),
                                elevation = 5.dp,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                color = Color.DarkGray,
                                                width = 1.5.dp,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable { navController.navigate("item/${item.itemIdx}") },
                                        model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                        contentDescription = item.itemName,
                                    )
                                    Column(
                                        modifier = Modifier
                                            .width(120.dp),
                                    ) {
                                        Text(
                                            text = "${item.itemName}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .padding(0.dp, 10.dp)
                                                .clickable { navController.navigate("item/${item.itemIdx}") },
                                            overflow = TextOverflow.Clip,

                                            )
                                        Column() {
                                            if (item.price == item.couponPrice) {
                                                Text(
                                                    text = "${DecimalFormat("#,###").format(item.price)}원",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            } else {
                                                Text(
                                                    text = "${DecimalFormat("#,###").format(item.price)}원",
                                                    color = Color.LightGray,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    textDecoration = TextDecoration.LineThrough,
                                                )
                                                Row(
                                                    modifier = Modifier.height(20.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.onsale),
                                                        modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp),
                                                        contentDescription = "할인중",
                                                        contentScale = ContentScale.Inside,
                                                    )
                                                    Text(
                                                        text = "${DecimalFormat("#,###").format(item.couponPrice)}원",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        OutlinedButton(
                                            onClick = {
//                                                quantity += 1
                                                      },
                                            modifier = Modifier.size(30.dp),
                                            shape = CircleShape,
                                        ) {
                                            Text(
                                                text = "+",
                                                overflow = TextOverflow.Visible,
                                            )
                                        }
//                                        Box (
//                                            modifier = Modifier
//                                                .size(30.dp)
//                                                .clip(RoundedCornerShape(10.dp))
//                                                .background(Color.LightGray),
//                                            contentAlignment = Alignment.Center,
//                                        ){
//                                        }
                                        TextField(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color.LightGray),
                                            value = quantity,
                                            onValueChange = { temp -> quantity = temp },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number
                                            )
                                        )
                                        OutlinedButton(
                                            onClick = {
//                                                quantity -= 1
                                                      },
                                            modifier = Modifier.size(30.dp),
                                            shape = CircleShape,
                                        ) {
                                            Text(
                                                text = "-",
                                                overflow = TextOverflow.Visible,
                                                )
                                        }
                                    }
                                    Image(
                                        painter = painterResource(R.drawable.delete),
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clickable { deleteGotCart(item.itemIdx) },
                                        contentDescription = "삭제",
                                    )
                                }
                            }
                        }
                    }
                    Text(text = "합계 : ${DecimalFormat("#,###").format(resultCart!!.total)}원")
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
