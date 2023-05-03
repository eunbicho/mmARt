package com.example.mmart

//import android.media.Image
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import java.net.URL
//
//class GotCartActivity : ComponentActivity() {
//
//    val BASE_URL = "http://k8a405.p.ssafy.io:8090/api/v1"
//    val userIdx = 1
//
//    var getGotCarts = URL(BASE_URL+"/gotcarts/${userIdx}").openConnection()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent{
//
//        }
//    }
//
//    @Composable
//    fun GotCartList() {
//
//    }
//
//    @Composable
//    fun GotCartItem() {
//        Row (modifier = Modifier.padding(all = 8.dp)) {
//            Image (
//                    )
//        }
//
//    }
//}