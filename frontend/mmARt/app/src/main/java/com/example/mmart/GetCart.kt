package com.example.mmart

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*

import kotlinx.coroutines.*
import java.text.DecimalFormat

@Composable
fun GetCart(navController: NavController){

    val userId = 1
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var resultCart: CartContent? by remember { mutableStateOf(null) }
    var resultCode: String? by remember { mutableStateOf(null) }
    var resultUser: UserInfo? by remember { mutableStateOf(null) }

    var showQrcode by remember { mutableStateOf(false)}
    var quantityError by remember { mutableStateOf(false)}
    var inventoryError by remember { mutableStateOf(false)}


    // 한 번만 실행
    LaunchedEffect(true) {
        val cartRes = coroutineScope.async { api.getCartsRead(userId) }.await()
        resultCart = cartRes.result
        resultCode = cartRes.resultCode
        val userRes = coroutineScope.async { api.getUser(userId) }.await()
        resultUser = userRes.result
    }

    fun checkQuantity(prev: Int, curr: TextFieldValue, item: ItemInfo): Int{
        if (curr.text.equals("") || curr.text.toInt() < 1){
            quantityError = true
            return prev
        } else if (curr.text.toInt() > item.inventory) {
            inventoryError = true
            return prev
        }
        return curr.text.toInt()
    }

    Column {
        topBar(navController = navController, "장볼구니")

        // result가 null이 아닐 경우만
        if(resultCart != null){
            if (resultCode == "SUCCESS") {
                if (resultCart!!.itemList.isNotEmpty()) {
                    var priceTotal = 0
                    var discountTotal = 0
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(560.dp)
                            .padding(20.dp, 0.dp)
                    ) {
                        items(resultCart!!.itemList) {
                                item ->
                            priceTotal += item.price
                            discountTotal += if (item.isCoupon) item.price - item.couponPrice else 0
                            var quantity by remember { mutableStateOf(TextFieldValue("${item.quantity}")) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .border(
                                        color = Dark_gray,
                                        width = 1.5.dp,
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                backgroundColor = Light_blue,
                                contentColor = Dark_gray,
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
                                                color = Dark_gray,
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
                                            fontSize = 15.sp,
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
                                                    color = Main_gray,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    textDecoration = TextDecoration.LineThrough,
                                                )
                                                Row(
                                                    modifier = Modifier.height(20.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.onsale),
                                                        modifier = Modifier
                                                            .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                                            .shadow(1.dp),
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
                                        Image(
                                            painter = painterResource(R.drawable.quantity_plus),
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clickable {
                                                    val tempQuantity = quantity.text.toInt() + 1
                                                    updateGetCart(item.itemIdx, tempQuantity)
                                                    quantity = TextFieldValue("${tempQuantity}")

                                                },
                                            contentDescription = "+",
                                        )
                                        BasicTextField(
                                            value = quantity,
                                            onValueChange = {
                                                if ( it.text == "" || it.text.toInt() <1 ) {
                                                    quantityError = true
                                                } else if ( it.text.toInt() > item.inventory ) {
                                                    inventoryError = true
                                                } else {
                                                    quantity = it
                                                }
                                            },
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color.White),
                                            textStyle = TextStyle(textAlign = TextAlign.Center),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number
                                            ),
                                            singleLine = true,
                                            cursorBrush = SolidColor(Light_gray),
                                            decorationBox = { innerTextField ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) { innerTextField() }
                                            }
                                        )

                                        Image(
                                            painter = painterResource(R.drawable.quantity_minus),
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clickable {
                                                    val tempQuantity = quantity.text.toInt() - 1
                                                    updateGetCart(item.itemIdx, tempQuantity)
                                                    quantity = TextFieldValue("${tempQuantity}")
                                                },
                                            contentDescription = "-",
                                        )
                                    }
                                    Image(
                                        painter = painterResource(R.drawable.delete),
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clickable { deleteGetCart(item.itemIdx) },
                                        contentDescription = "삭제",
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(20.dp, 0.dp)
                    ) {
                        Divider(thickness = 2.dp, color = Dark_gray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "총 상품 가격",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Main_gray,
                            )
                            Text(
                                text = "${DecimalFormat("#,###").format(priceTotal)}원",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Main_gray,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "총 할인 금액",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Main_blue,
                            )
                            Text(
                                text = "${DecimalFormat("#,###").format(discountTotal)}원",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Main_blue,
                            )
                        }
                        Divider(thickness = 1.dp, color = Light_gray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "총 결제 금액",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Dark_gray,
                            )
                            Text(
                                text = "${DecimalFormat("#,###").format(resultCart!!.total)}원",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Dark_gray,
                            )
                        }
                    }
                } else {
                    Text("장볼구니가 비어있습니다.")
                }
            } else {
                Text("장볼구니를 찾을 수 없습니다.")
            }
        }
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            FloatingActionButton(onClick = {

            }) {
                Image(
                    painter = painterResource(R.drawable.top),
                    contentDescription = "TOP",
                )
            }

            FloatingActionButton(onClick = { showQrcode = true }) {
                Image(
                    painter = painterResource(R.drawable.pay),
                    contentDescription = "PAY",
                )
            }

        }
    }

    if (showQrcode) {
        Dialog(
            onDismissRequest = { showQrcode = false },
            content = {
                Card (
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 10.dp
                ){
                    Column(
                        modifier = Modifier.padding(20.dp),
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

    if (quantityError) {
        Dialog(
            onDismissRequest = { quantityError = false },
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = 10.dp
                ){
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("수량을 다시 한번 확인해주세요.")
                        Button(
                            onClick = { quantityError = false }
                        ){
                            Text(text = "닫기")
                        }
                    }
                }
            }
        )
    }

    if (inventoryError) {
        Dialog(
            onDismissRequest = { inventoryError = false },
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = 10.dp
                ){
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("지점 보유 재고를 초과하였습니다.")
                        Button(
                            onClick = { inventoryError = false }
                        ){
                            Text(text = "닫기")
                        }
                    }
                }
            }
        )
    }



}

fun updateGetCart(itemIdx: Int, quantity: Int) {
    println("${itemIdx}, ${quantity}")
}

fun deleteGetCart(itemIdx: Int) {
    println(itemIdx)
}

//@Preview(showBackground = true)
//@Composable
//fun GetCartPreview(){
//    GetCart(rememberNavController())
//}