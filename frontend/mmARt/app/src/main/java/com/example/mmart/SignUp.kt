package com.example.mmart

import androidx.compose.foundation.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*

import kotlinx.coroutines.*
import retrofit2.HttpException

@Composable
fun SignUp(navController: NavController){

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    var id: String by remember{ mutableStateOf("") }
    var name: String by remember{ mutableStateOf("") }
    // 중복 여부
    var isDuplicate: Boolean by remember { mutableStateOf(true) }
    var password: String by remember{ mutableStateOf("") }
    // 알림 모달
    var isWrong: Boolean by remember { mutableStateOf(false) }
    // 알림 모달 내용
    var alertText: String by remember { mutableStateOf("확인 후 다시 시도해 주세요") }
    // 회원가입 성공
    var isDone: Boolean by remember { mutableStateOf(false) }

    // 중복 확인
    fun duplicationCheck(){
        if(id==""){
            alertText = "아이디를 입력해 주세요"
            isWrong = true
        } else {
            coroutineScope.launch{
                try {
                    val response = api.duplicationCheck(id)
                    // 이미 있는 아이디이면
                    if(response.resultCode == "SUCCESS") {
                        isDuplicate = true
                        alertText = "중복된 아이디입니다."
                        isWrong = true
                    }
                } catch (e: Exception){
                    println("중복 확인 에러////////////////////////")
                    e.printStackTrace()
                    isDuplicate = false
                    alertText = "사용할 수 있는 아이디입니다."
                    isWrong = true
                }
            }
        }
    }

    // 회원가입
    fun signUp(){
        if(id==""){
            alertText = "아이디를 입력해 주세요."
            isWrong = true
        } else if(isDuplicate) {
            alertText = "아이디 중복 확인을 해주세요."
            isWrong = true
        } else if (name==""){
            alertText = "이름을 입력해 주세요."
            isWrong = true
        } else if(password==""){
            alertText = "비밀번호를 입력해 주세요."
            isWrong = true
        } else{
            val body = mapOf(
                "email" to id,
                "name" to name,
                "password" to password
            )
            coroutineScope.launch{
                try {
                    val response = api.signUp(body)
                    if(response.resultCode == "SUCCESS") {
                        alertText = "회원가입에 성공했습니다."
                        isWrong = true
                        isDone = true
                    }
                } catch (e: Exception){
                    println("signUp 에러////////////////////////")
                    e.printStackTrace()
                    alertText = "중복된 아이디입니다"
                    isWrong = true
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Light_blue)
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 60.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.mmart_logo), contentDescription = "로고", modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp))

        // 아이디 입력
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            shape = CircleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                cursorColor = Vivid_blue,
                trailingIconColor = Dark_gray,
                focusedBorderColor = Vivid_blue,
                placeholderColor = Light_gray
            ),
            placeholder = {Text("아이디")},
            singleLine = true,
            modifier = Modifier.padding(top=10.dp, bottom = 3.dp),
            trailingIcon = {
                OutlinedButton(
                    onClick = { duplicationCheck() },
                    shape = CircleShape,
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Text("중복확인", fontSize = 12.sp, color = Vivid_blue)
                }
            }
        )
        if(isDuplicate){
            Text("중복 확인을 해주세요.", color = Color.Red, fontSize = 10.sp, textAlign = TextAlign.Start, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp))
        } else {
            Text("", fontSize = 10.sp)
        }

        // 이름 입력
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            shape = CircleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                cursorColor = Vivid_blue,
                focusedBorderColor = Vivid_blue,
                placeholderColor = Light_gray
            ),
            placeholder = {Text("이름")},
            singleLine = true,
            modifier = Modifier.padding(10.dp)
        )

        // 비밀번호 입력
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = CircleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                cursorColor = Vivid_blue,
                focusedBorderColor = Vivid_blue,
                placeholderColor = Light_gray
            ),
            placeholder = {Text("비밀번호")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, bottom = 30.dp)
        )

        Row(
            modifier = Modifier.padding(vertical = 0.dp, horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { signUp() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Vivid_blue),
                modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2.5f).padding(10.dp)
            ) {
                Text("확인", color = Color.White)
            }
            Button(
                onClick = { navController.popBackStack() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Vivid_yellow),
                modifier = Modifier.fillMaxWidth(2f).aspectRatio(2.5f).padding(10.dp)
            ) {
                Text("취소", color = Color.White)
            }
        }
    }

    // 아이디, 비밀번호 확인 모달
    if (isWrong) {
        AlertDialog(
            onDismissRequest = { isWrong = false },
            text = { Text(alertText, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 18.sp) },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedButton(
                        onClick = {
                            if(isDone){
                                navController.popBackStack()
                            } else {
                                isWrong = false
                            } },
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