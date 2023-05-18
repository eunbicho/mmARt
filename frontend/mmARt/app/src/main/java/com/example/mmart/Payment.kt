package com.example.mmart

import android.media.Image
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Main_blue
import com.example.mmart.ui.theme.Main_gray
import com.example.mmart.ui.theme.Vivid_blue
import kotlinx.coroutines.*

@Composable
fun Payment(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 상단바
        topBar(navController, "결제 내역")

        if(payments != null) {
            if(payments!!.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    blankView("결제 내역이 없습니다.")
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { navController.navigate("main") }
                        ) {
                            Image(painter = painterResource(R.drawable.main), contentDescription = "홈으로", Modifier.size(80.dp))
                            Text("홈으로", Modifier.padding(5.dp))
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()){
                    LazyColumn(state = listState, contentPadding = PaddingValues(bottom=90.dp), modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)){
                        items(payments!!){
                                payment ->
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical=10.dp)
                                    .border(
                                        width = (1.8).dp,
                                        color = Main_gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ){
                                Box(
                                    modifier = Modifier
                                        .padding(30.dp)
                                        .fillMaxWidth()
                                ) {
                                    Column{
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${payment.marketName}점")
                                            Text("${payment.total}원")
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 20.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Text(
                                                text = "${payment.date[0]}. ${payment.date[1]}. ${payment.date[2]}",
                                                fontWeight = FontWeight.Bold,
                                                color = Main_gray,
                                            )
                                            Text(
                                                text = "주문 상세보기 >",
                                                color = Vivid_blue,
                                                modifier = Modifier
                                                    .clickable { navController.navigate("payment/${payment.paymentIdx}") }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // 하단 버튼
                    floatingBtn(listState = listState)
                }
            }
        }
    }
}
