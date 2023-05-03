package com.example.mmart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mmart.ui.theme.Main_yellow
import com.example.mmart.ui.theme.mainFont

// 검색바
@Composable
fun searchBar(){
    val navController = rememberNavController()
    var searchWord by remember { mutableStateOf("") }

    fun search(){
        println(searchWord)
        searchWord = ""
    }

    fun barcodeSearch(){
       navController.navigate("barcodeScan")
    }

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
        keyboardActions = KeyboardActions(
            onSearch = {search()}
        )
    )
}
