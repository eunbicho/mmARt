package com.example.mmart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mmart.ui.theme.Main_yellow
import com.example.mmart.ui.theme.mainFont
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mmart.ui.theme.Main_blue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable(route = "main"){
                    Main(navController)
                }
                composable(route = "category/{categoryId}", arguments = listOf(navArgument("categoryId"){type = NavType.IntType})){ backStackEntry ->
                    Category(navController, backStackEntry.arguments?.getInt("categoryId"))
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Main(navController: NavController) {

    // 카테고리 별 상품보기
    fun category(categoryId: Int){
        navController.navigate("category/$categoryId")
    }


    Scaffold(

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
            ){
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
        searchBar()

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
                LazyRow(){
                    items(4){
                        item ->
                        Text(text = "${item+1}")
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clickable { category(1) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "가공식품"
                        Image(
                            painter = painterResource(R.drawable.category_1),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }

                    Column(
                        modifier = Modifier
                            .clickable { category(2) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "신선식품"
                        Image(
                            painter = painterResource(R.drawable.category_2),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { category(3) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "일상용품"
                        Image(
                            painter = painterResource(R.drawable.category_3),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { category(4) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "의약품"
                        Image(
                            painter = painterResource(R.drawable.category_4),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .clickable { category(5) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "교육/문화용품"
                        Image(
                            painter = painterResource(R.drawable.category_5),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { category(6) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "디지털"
                        Image(
                            painter = painterResource(R.drawable.category_6),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { category(7) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "가구/인테리어"
                        Image(
                            painter = painterResource(R.drawable.category_7),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                    Column(
                        modifier = Modifier
                            .clickable { category(8) },
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        val description = "스포츠"
                        Image(
                            painter = painterResource(R.drawable.category_8),
                            contentDescription = description,
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(description, fontFamily = mainFont)
                    }
                }
            }
        }
    }

    }


}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    val navController = rememberNavController()
    Main(navController = navController)
}