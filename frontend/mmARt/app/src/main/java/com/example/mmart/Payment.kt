package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.AsyncImage
import kotlinx.coroutines.*

@Composable
fun Payment(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    var payments: List<Payment>? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        try {
            payments = coroutineScope.async { api.getPayments(userId) }.await().result
        } catch (e: Exception){
            println("결제 내역 조회 에러-----------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    Column() {
        // 상단바
        topBar(navController, "결제 내역 조회")

        if(payments != null) {
            if(payments!!.isEmpty()){
                Text("결제 내역이 없습니다.")
            } else {
                LazyColumn(){
                    items(payments!!){
                        payment ->
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clickable { navController.navigate("payment/${payment.paymentIdx}") }
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${payment.marketName}점")
                                Text("${payment.total}원")
                            }
                            Text("${payment.date[0]}.${payment.date[1]}.${payment.date[2]}.${payment.date[3]}:${payment.date[4]}")

                            Divider(
                                color = Color.Black,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(10.dp)
                            )
                        }

                    }
                }
            }
        }
    }
}
