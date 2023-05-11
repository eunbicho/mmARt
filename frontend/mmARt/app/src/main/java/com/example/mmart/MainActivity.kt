package com.example.mmart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material.MaterialTheme
import com.example.mmart.ui.theme.mainTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(typography = mainTypography) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    // 메인 화면
                    composable(route = "main") {
                        Main(navController)
                    }
                    //카테고리 별 상품 보기
                    composable(
                        route = "category/{categoryIdx}",
                        arguments = listOf(navArgument("categoryIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        Category(navController, backStackEntry.arguments?.getInt("categoryIdx"))
                    }
                    // 바코드 스캔
                    composable(route = "barcodeScan") {
                        BarcodeScan(navController)
                    }
                    // 장볼구니
                    composable(route = "getCart") {
                        GetCart(navController)
                    }
                    // 장봤구니
                    composable(route = "gotCart") {
                        GotCart(navController)
                    }
                    // 마이페이지
                    composable(route = "myPage") {
                        MyPage(navController)
                    }
                    // 유니티 테스트
                    //                composable(route = "unity"){
                    //                    UnityTest(navController)
                    //                }
                    // 상품 상세
                    composable(
                        route = "item/{itemIdx}",
                        arguments = listOf(navArgument("itemIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        ItemDetail(navController, backStackEntry.arguments?.getInt("itemIdx"))
                    }
                    // 검색 결과
                    composable(
                        route = "search/{searchWord}",
                        arguments = listOf(navArgument("searchWord") { type = NavType.StringType })
                    ) { backStackEntry ->
                        Search(navController, backStackEntry.arguments!!.getString("searchWord")!!)
                    }
                    // 마이페이지 - 결제 내역 조회
                    composable(route = "payment") {
                        Payment(navController)
                    }
                    // 마이페이지 - 결제 내역 조회 - 상세 조회
                    composable(
                        route = "payment/{paymentIdx}",
                        arguments = listOf(navArgument("paymentIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        PaymentDetail(navController, backStackEntry.arguments!!.getInt("paymentIdx"))
                    }
                    // 마이페이지 - 결제 내역 조회 - 상세 조회 - 리뷰 작성
                    composable(
                        route = "reviewCreate/{paymentDetailIdx}",
                        arguments = listOf(navArgument("paymentDetailIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        ReviewCreate(navController, backStackEntry.arguments!!.getInt("paymentDetailIdx"))
                    }
                    // 마이페이지 - 리뷰 내역 조회
                    composable(route = "review") {
                        Review(navController)
                    }
                    // 마이페이지 - 리뷰 내역 조회 - 리뷰 수정
                    composable(
                        route = "reviewUpdate/{reviewIdx}",
                        arguments = listOf(navArgument("reviewIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        ReviewUpdate(navController, backStackEntry.arguments!!.getInt("reviewIdx"))
                    }
                }
            }
        }
    }
}

// 유저 아이디
var userId = 1

@SuppressLint("UnusedMaterialScaffoldPaddingParameter") // Scaffold의 padding value 사용 안 할 때
@Composable
fun Main(navController: NavController) {

    // 카테고리 별 상품보기
    fun category(categoryId: Int){
        navController.navigate("category/$categoryId")
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Image(painter = painterResource(R.drawable.bottombar_1), contentDescription = "빠른장보기",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("barcodeScan") })
                )
                Image(painter = painterResource(R.drawable.bottombar_2), contentDescription = "장볼구니",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("getCart") }))
                Image(painter = painterResource(R.drawable.bottombar_3), contentDescription = "장봤구니",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("gotCart") }))
                Image(painter = painterResource(R.drawable.bottombar_4), contentDescription = "마이페이지",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("myPage") }))
            }
        }
    ) {
        // 배경 이미지
        Image(
            painter = painterResource(R.drawable.bg),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "배경",
            contentScale = ContentScale.FillBounds
        )

        // 상단 로고, 지점 표시
        Box {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "로고",
                    modifier = Modifier
                        .padding(16.dp)
                        .width(150.dp)
                        .height(100.dp)
                )
                Image(
                    painter = painterResource(R.drawable.place),
                    contentDescription = "지점",
                    modifier = Modifier
                        .padding(16.dp)
                        .width(100.dp)
                        .height(100.dp)
                )

            }
        }
        // 바디
        Column(
            modifier = Modifier
                .padding(top = 160.dp),
        ) {
            // 검색
            searchBar(navController)

            // 카테고리 부분
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .height(200.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween

                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    ) {
                        items(4) { item ->
                            fun imageResource(num: Int): Int {
                                return when (num) {
                                    0 -> R.drawable.category_1
                                    1 -> R.drawable.category_2
                                    2 -> R.drawable.category_3
                                    3 -> R.drawable.category_4
                                    else -> R.drawable.category_1
                                }
                            }

                            fun description(num: Int): String {
                                return when (num) {
                                    0 -> "가공식품"
                                    1 -> "신선식품"
                                    2 -> "일상용품"
                                    3 -> "의약품"
                                    else -> "가공식품"
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .clickable { category(item + 1) },
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Image(
                                    painter = painterResource(imageResource(item)),
                                    contentDescription = description(item),
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                                Text(description(item))
                            }
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    ) {
                        items(4) { item ->
                            fun imageResource(num: Int): Int {
                                return when (num) {
                                    0 -> R.drawable.category_5
                                    1 -> R.drawable.category_6
                                    2 -> R.drawable.category_7
                                    3 -> R.drawable.category_8
                                    else -> R.drawable.category_1
                                }
                            }

                            fun description(num: Int): String {
                                return when (num) {
                                    0 -> "교육용품"
                                    1 -> "디지털"
                                    2 -> "인테리어"
                                    3 -> "스포츠"
                                    else -> "가공식품"
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .clickable { category(item + 5) },
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Image(
                                    painter = painterResource(imageResource(item)),
                                    contentDescription = description(item),
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                                Text(description(item))
                            }
                        }
                    }
                }
            }
            Button(onClick = {  }, modifier = Modifier.height(50.dp)){ Text(text = "유니티테스트") }
//          Button(onClick = { navController.navigate("unity") }){ Text(text = "유니티테스트") }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview(){
    val navController = rememberNavController()
    Main(navController = navController)
}