package com.example.mmart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import kotlinx.coroutines.async
import java.text.DecimalFormat


@Composable
fun GetCart(navController: NavController) {
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val textFocus = LocalFocusManager.current

    var reload: Boolean by remember { mutableStateOf(false) }
    var cartRes: CartContent? by remember { mutableStateOf(null) }
//    var userRes: UserInfo? by remember { mutableStateOf(null) }

    var findMap by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var inventoryError by remember { mutableStateOf(false) }


    // 한 번만 실행
    LaunchedEffect(reload) {
        try {
            cartRes = coroutineScope.async { api.getGetCarts(userId) }.await().result
//            userRes = coroutineScope.async { api.getUser(userId) }.await().result
        } catch (e: Exception) {
            println("----------CART ERROR----------")
            e.printStackTrace()
            println("------------------------------")
        }
    }

    fun updateGetCart(itemIdx: Int, quantity: Int) {
        try {
            val cartReq = mapOf(
                "itemIdx" to itemIdx,
                "quantity" to quantity,
                "userIdx" to userId,
            )
            coroutineScope.async { api.updateGetCart(cartReq) }
        } catch (e: Exception) {
            println("----------UPDATE ERROR--------")
            e.printStackTrace()
            println("------------------------------")
        }
        reload = !reload
    }

    fun deleteGetCart(itemIdx: Int) {
        try {
            coroutineScope.async { api.deleteGetCart(userId, itemIdx) }
        } catch (e: Exception) {
            println("----------DELETE ERROR--------")
            e.printStackTrace()
            println("------------------------------")
        }
        reload = !reload
    }

    Column {
        topBar(navController = navController, "장볼구니")
        Row(
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            searchBar(navController = navController)
        }

        // result가 null이 아닐 경우만
        if(cartRes != null){
            if (cartRes!!.itemList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                        .padding(20.dp, 0.dp),
                    state = listState,
                ) {
                    items(cartRes!!.itemList) {
                            item ->
                        var quantity: TextFieldValue? by remember { mutableStateOf(TextFieldValue("${item.quantity}")) }
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
                                        text = item.itemName,
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
                                    IconButton(
                                        onClick = {
                                            val tempQuantity = quantity!!.text.trim().toInt() + 1
                                            updateGetCart(item.itemIdx, tempQuantity)
                                            quantity = TextFieldValue(tempQuantity.toString())
                                        },
                                        enabled = quantity!!.text.trim().isNotEmpty() && quantity!!.text.trim().toIntOrNull() != null && item.inventory > quantity!!.text.trim().toInt(),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.quantity_plus),
                                            contentDescription = "+",
                                        )
                                    }

                                    BasicTextField(
                                        value = quantity!!,
                                        onValueChange = { quantity = it },
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color.White),
                                        textStyle = TextStyle(textAlign = TextAlign.Center),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                if ( quantity == null || quantity!!.text.trim().isNullOrEmpty() || quantity!!.text.trim().toIntOrNull() == null || quantity!!.text.trim().toInt() < 1 ) {
                                                    quantityError = true
                                                    quantity = TextFieldValue(item.quantity.toString())
                                                } else if ( quantity!!.text.trim().toInt() > item.inventory ) {
                                                    inventoryError = true
                                                    quantity =
                                                        TextFieldValue(item.quantity.toString())
                                                } else {
                                                    updateGetCart(item.itemIdx, quantity!!.text.trim().toInt())
                                                }
                                                textFocus.clearFocus()
                                            },
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

                                    IconButton(
                                        onClick = {
                                            val tempQuantity = quantity!!.text.trim().toInt() - 1
                                            updateGetCart(item.itemIdx, tempQuantity)
                                            quantity = TextFieldValue(tempQuantity.toString())
                                        },
                                        enabled = quantity!!.text.trim().isNotEmpty() && quantity!!.text.trim().toIntOrNull() != null && 1 < quantity!!.text.trim().toInt(),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.quantity_minus),
                                            contentDescription = "-",
                                        )
                                    }
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
                            text = "${DecimalFormat("#,###").format(cartRes!!.priceTotal)}원",
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
                            text = "${DecimalFormat("#,###").format(cartRes!!.discountTotal)}원",
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
                            text = "${DecimalFormat("#,###").format(cartRes!!.total)}원",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Dark_gray,
                        )
                    }
                }
                floatingBtns(
                    listState = listState,
                    secondBtn = R.drawable.findway,
                    secondBtnName = "MAP",
                    secondEvent = { findMap = true }
                )
            } else {
                blankView("장볼구니가 비어있습니다.")
            }

        }
    }

    if (findMap) {

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
