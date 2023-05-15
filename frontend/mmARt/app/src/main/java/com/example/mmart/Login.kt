package com.example.mmart

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.Vivid_blue
import com.example.mmart.ui.theme.Vivid_yellow

import kotlinx.coroutines.*
import retrofit2.HttpException

@Composable
fun Login(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    var id: String by remember{ mutableStateOf("") }
    var password: String by remember{ mutableStateOf("") }
    var isWrong: Boolean by remember { mutableStateOf(false) }

    fun login(){
        val body = mapOf(
            "email" to id,
            "password" to password
        )
        coroutineScope.launch{
            try {
                val response = api.login(body)
                if(response.resultCode == "SUCCESS") {
                    userId = response.result.userIdx
                    navController.navigate(
                        "main",
                        NavOptions.Builder()
                            .setPopUpTo("login", true)
                            .build()
                    )
                }
            } catch (e: Exception){
                isWrong = true
            }
        }
    }

    // 배경 이미지
    Image(
        painter = painterResource(R.drawable.bg),
        modifier = Modifier.fillMaxSize(),
        contentDescription = "배경",
        contentScale = ContentScale.FillBounds
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize() .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.logo), contentDescription = "로고", modifier = Modifier.padding(vertical = 20.dp))

        // 아이디 입력
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            shape = CircleShape,
            placeholder = {Text("아이디", color = Color.LightGray)},
            singleLine = true,
            modifier = Modifier.padding(10.dp)
        )

        // 비밀번호 입력
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = CircleShape,
            placeholder = {Text("비밀번호", color = Color.LightGray)},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.padding(10.dp)
        )
        
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { login() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Vivid_blue),
                modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2f).padding(10.dp)
            ) {
                Text("로그인", color = Color.White)
            }
            Button(
                onClick = { navController.navigate("signUp") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Vivid_yellow),
                modifier = Modifier.fillMaxWidth(1F).aspectRatio(2f).padding(10.dp)
            ) {
                Text("회원가입", color = Color.White)
            }
        }
    }
    
    // 아이디, 비밀번호 확인 모달
    if (isWrong) {
        AlertDialog(
            onDismissRequest = { isWrong = false },
            text = { Text("아이디, 비밀번호 확인 후\n\n다시 로그인 해주세요", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 18.sp) },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom=10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedButton(
                        onClick = { isWrong = false },
                        elevation = ButtonDefaults.elevation(1.dp)
                    ) {
                        Text("확인", color = Color.Black)
                    }
                }
            },
            modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp)
        )
    }

}