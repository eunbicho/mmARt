package com.example.mmart

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

import kotlinx.coroutines.*

@Composable
fun BarcodeScan(navController: NavHostController) {
    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    var scanFlag by remember { mutableStateOf(false) }

    // 바코드 스캔한 아이템
    var item: ItemInfo? by remember { mutableStateOf(null) }
    // 아이템 확인 모달
    var itemCheck: Boolean by remember { mutableStateOf(false) }
    // 장봤구니 확인 모달
    var gotCartCheck: Boolean by remember { mutableStateOf(false) }

    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            this.resume()
            capture.decode()
            this.decodeContinuous { result ->
                if(scanFlag){
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let {
                    // 바코드 검색
                    coroutineScope.launch{
                        try {
                            val response = api.getItemByBarcode(it)
                            if(response.resultCode == "SUCCESS") {
                                item = response.result
                                itemCheck = true
                            }
                        } catch (e: Exception){
                            println("바코드 검색 에러-------------------")
                            e.printStackTrace()
                            scanFlag = false
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(key1 = "someKey" ){
        compoundBarcodeView.resume()
        onDispose {
            compoundBarcodeView.pause()
        }
    }

    fun addGotCart(){
        val body = mapOf(
            "itemIdx" to item!!.itemIdx,
            "quantity" to 1,
            "userIdx" to userId
        )
        coroutineScope.launch{
            try {
                val response = api.addGotCart(body)
                if(response.resultCode == "SUCCESS") {
                    gotCartCheck = true
                }
            } catch (e: Exception){
                println("장봤구니 추가 에러-------------------")
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 상단바
        topBar(navController = navController, "바코드 스캔")

        AndroidView(
            modifier = Modifier,
            factory = { compoundBarcodeView },
        )
    }

    // 상세보기 or 장봤구니 추가
    if(itemCheck) {
        Dialog(
            onDismissRequest = {
                itemCheck = false
                scanFlag = false
            }
        ) {
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(40.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(40.dp))
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("상품명: ${item!!.itemName}", modifier = Modifier.padding(30.dp,30.dp,30.dp,20.dp), fontSize = 20.sp, textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { navController.navigate("item/${item!!.itemIdx}") }
                        ) {
                            Image(painter = painterResource(R.drawable.detail), contentDescription = "상품 상세 보기", Modifier.size(70.dp))
                            Text("상세보기", Modifier.padding(5.dp), fontSize = 15.sp)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    itemCheck = false
                                    addGotCart()
                                }
                        ) {
                            Image(painter = painterResource(R.drawable.gotcart), contentDescription = "장봤구니 추가", Modifier.size(70.dp))
                            Text("장봤구니 추가", Modifier.padding(5.dp), fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }

    // 장봤구니 모달
    if(gotCartCheck){
        Dialog(
            onDismissRequest = {
                gotCartCheck = false
                scanFlag = false
            }
        ) {
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(40.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(40.dp))
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("상품이 장봤구니에\n추가되었습니다", modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp), fontSize = 20.sp, textAlign = TextAlign.Center)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    gotCartCheck = false
                                    scanFlag = false
                                }
                        ) {
                            Image(painter = painterResource(R.drawable.previous), contentDescription = "이전으로", Modifier.size(70.dp))
                            Text("이전으로", Modifier.padding(5.dp))
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { navController.navigate("gotCart") }
                        ) {
                            Image(painter = painterResource(R.drawable.gotcart), contentDescription = "장봤구니로", Modifier.size(70.dp))
                            Text("장봤구니로", Modifier.padding(5.dp))
                        }
                    }
                }
            }
        }
    }
}
