package com.example.mmart

import android.app.appsearch.SearchResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import com.example.mmart.ui.theme.Main_yellow
import com.example.mmart.ui.theme.mainFont
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 검색바
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun searchBar(navController: NavController){
    var searchWord by remember { mutableStateOf("") }

    fun search(){
        navController.navigate("search/$searchWord")
    }

    fun barcodeSearch(){
        navController.navigate("barcodeScan")
    }

    // 키보드 조정
    val keyboardController = LocalSoftwareKeyboardController.current

    // 검색창
    OutlinedTextField(
        value = searchWord,
        onValueChange = { searchWord = it },
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp)
            .border(color = Color.Black, width = 1.5.dp, shape = CircleShape)
            .shadow(
                shape = CircleShape,
                elevation = 5.dp,
                ambientColor = Color.Black,
                clip = true
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Main_yellow,
            textColor = Color.Black
        ),
        singleLine = true,
        textStyle = TextStyle(fontFamily = mainFont, fontSize = 15.sp),

        // textfield 우측에 아이콘 추가
        trailingIcon = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.barcode),
                    contentDescription = "바코드 검색",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { barcodeSearch() }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            search()
                        }
                )

            }
        },
        // 키보드 모양 바꾸기
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        // 엔터(키보드에서 search아이콘) 클릭 시 실행
        keyboardActions = KeyboardActions(onSearch = {
            search()
            keyboardController?.hide()
        })
    )
}

@Composable
fun topBar(navController: NavController, title: String){

    Row (
        modifier = Modifier
            .background(color = Color(0XFFF5F5F5))
            .fillMaxWidth()
            .height(80.dp)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ){
        Image(painter = painterResource(R.drawable.back), contentDescription = "뒤로 가기", modifier = Modifier
            .clickable {
                navController.previousBackStackEntry?.let {
                    navController.popBackStack(it.destination.id, false)
                }
            }
        )
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.Light)
        Image(painter = painterResource(R.drawable.home), contentDescription = "홈으로", modifier = Modifier
            .clickable { navController.navigate("main") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToolPreview(){
    val navController = rememberNavController()
    topBar(navController = navController, "상세보기")
}
