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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

import kotlinx.coroutines.*

@Composable
fun MyPage(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    // 유저 정보
    var user: UserInfo? by remember { mutableStateOf(null) }
    // 로그아웃 모달
    var isLogout: Boolean by remember { mutableStateOf(false) }
    //로딩창
    var isLoading: Boolean by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        // 유저 정보 조회
        try {
            user = coroutineScope.async { api.getUser(userId) }.await().result
        } catch (e: Exception){
            println("마이페이지 유저 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }

    }
    Column() {
        topBar(navController = navController, "마이페이지")
        if(user != null){
            isLoading = false
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
                .padding(30.dp)
                .align(CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${user!!.name}님", fontSize = 30.sp)
                Text("로그아웃", fontSize = 15.sp, fontWeight = FontWeight.Light, modifier = Modifier
                    .clickable { isLogout = true }
                    .height(IntrinsicSize.Min)
                )
            }
        }

        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(15.dp)
        )

        // 결제 내역 조회
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { navController.navigate("payment") },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "결제 내역 조회", fontSize = 20.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Icon(Icons.Default.KeyboardArrowRight, "결제 내역 조회 이동", modifier = Modifier.size(50.dp))
        }

        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(15.dp)
        )

        // 리뷰 내역 조회
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { navController.navigate("review") },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "리뷰 내역 조회", fontSize = 20.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Icon(Icons.Default.KeyboardArrowRight, "리뷰 내역 조회 이동", modifier = Modifier.size(50.dp))
        }

        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(15.dp)
        )

    }
    if (isLogout) {
        AlertDialog(
            onDismissRequest = { isLogout = false },
            text = { Text("로그아웃 하시겠습니까?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 18.sp) },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        onClick = {
                            userId = 0
                            navController.navigate("login"){
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                                  },
                        elevation = ButtonDefaults.elevation(1.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text("확인", color = Color.Black)
                    }
                    OutlinedButton(
                        onClick = { isLogout = false },
                        elevation = ButtonDefaults.elevation(1.dp),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text("취소", color = Color.Black)
                    }
                }
            },
            modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp)
        )
    }

    if(isLoading){
        loadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview(){
    val navController = rememberNavController()
    MyPage(navController = navController)
}