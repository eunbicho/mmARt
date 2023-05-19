package com.example.mmart

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.async
import java.text.DecimalFormat

@Composable
fun PaymentDetail(navController: NavController, paymentIdx: Int) {

    val api = APIS.create()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var payment: Payment? by remember { mutableStateOf(null) }
    var paymentDetails: List<PaymentDetail>? by remember { mutableStateOf(null) }
    var priceTotal = 0
    // 로딩창
    var isLoading: Boolean by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        try {
            payment = coroutineScope.async { api.getPayment(paymentIdx, userId) }.await().result
            paymentDetails = coroutineScope.async { api.getPaymentDetails(userId, paymentIdx) }.await().result
        } catch (e: Exception) {
            println("결제 내역 상세 조회 에러-------------")
            e.printStackTrace()
            println("---------------------------------")
        }
    }

    Column {
        // 상단바
        topBar(navController, "결제 내역 상세")

         if (payment != null && paymentDetails != null) {
             paymentDetails!!.forEach { priceTotal += it.item.price * it.quantity }
             Box(modifier = Modifier.fillMaxSize()) {
                 LazyColumn (
                     modifier = Modifier.fillMaxWidth().padding(30.dp),
                     state = listState,
                     contentPadding = PaddingValues(bottom=90.dp),
                 ) {
                     item() {
                         Column(
                             modifier = Modifier
                                 .fillMaxSize()
                                 .paint(painter = painterResource(R.drawable.receipt_top), contentScale = ContentScale.FillBounds)
                         ) {
                             Row(
                                 verticalAlignment = Alignment.Bottom,
                                 horizontalArrangement = Arrangement.Center,
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(30.dp)
                             ) {
                                 Image(painter = painterResource(R.drawable.mmart_logo), contentDescription = "mmart", Modifier.size(100.dp))
                                 Column(
                                     horizontalAlignment = Alignment.End,
                                     verticalArrangement = Arrangement.Center,
                                     modifier = Modifier.padding(15.dp)
                                 ) {
                                     Text(
                                         text = "싸피마트 ${payment!!.marketName}점",
                                         color = Main_gray,
                                         fontSize = 20.sp,
                                     )
                                     Text(
                                         text = "${payment!!.date[0]}-${DecimalFormat("00").format(payment!!.date[1])}-${DecimalFormat("00").format(payment!!.date[2])} ${DecimalFormat("00").format(payment!!.date[3])}:${DecimalFormat("00").format(payment!!.date[4])}",
                                         color = Main_gray,
                                         fontSize = 15.sp,
                                         fontWeight = FontWeight.Light
                                     )
                                 }
                             }
                             Divider(
                                 thickness = 2.dp,
                                 color = Light_gray,
                                 modifier = Modifier
                                     .padding(20.dp, 0.dp, 20.dp, 20.dp)
                             )
                         }
                     }
                     items(paymentDetails!!) { paymentDetail ->
                         Column(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .paint(painter = painterResource(R.drawable.receipt_content), contentScale = ContentScale.FillWidth),
                         ) {
                             // 상품 정보
                             Box(
                                 modifier = Modifier
                                     .fillMaxWidth()
                             ) {
                                 Column {
                                     Row(
                                         modifier = Modifier.padding(20.dp, 0.dp)
                                     ) {
                                         AsyncImage(
                                             modifier = Modifier
                                                 .size(65.dp)
                                                 .padding(5.dp)
                                                 .clip(RoundedCornerShape(10.dp))
                                                 .border(
                                                     color = Main_gray,
                                                     width = 1.5.dp,
                                                     shape = RoundedCornerShape(10.dp)
                                                 )
                                                 .clickable { navController.navigate("item/${paymentDetail.item.itemIdx}") },
                                             model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${paymentDetail.item.thumbnail}",
                                             contentDescription = paymentDetail.item.itemName,
                                             onSuccess = {isLoading = false}
                                         )
                                         Column(
                                             modifier = Modifier
                                                 .align(Alignment.CenterVertically)
                                                 .padding(start = 15.dp)
                                         ) {
                                             Text(
                                                 text = paymentDetail.item.itemName,
                                                 fontSize = 15.sp,
                                                 modifier = Modifier
                                                     .padding(bottom = 5.dp)
                                                     .clickable { navController.navigate("item/${paymentDetail.item.itemIdx}") }
                                             )
                                             Row(
                                                 horizontalArrangement = Arrangement.SpaceBetween,
                                                 modifier = Modifier.fillMaxWidth()
                                             ) {
                                                 Text(
                                                     text = DecimalFormat("#,###").format(
                                                         paymentDetail.item.price
                                                     ),
                                                     fontWeight = FontWeight.Light,
                                                     fontSize = 13.sp
                                                 )
                                                 Text(
                                                     text = paymentDetail.quantity.toString(),
                                                     fontWeight = FontWeight.Light,
                                                     fontSize = 13.sp
                                                 )
                                                 Text(
                                                     text = DecimalFormat("#,###").format(
                                                         paymentDetail.item.price * paymentDetail.quantity
                                                     ),
                                                     fontWeight = FontWeight.Light,
                                                     fontSize = 13.sp
                                                 )
                                             }
                                             if (paymentDetail.item.isCoupon) {
                                                 Text(
                                                     text = DecimalFormat("#,###").format(
                                                         paymentDetail.totalPrice
                                                     ),
                                                     fontWeight = FontWeight.Bold,
                                                     fontSize = 15.sp,
                                                     color = Vivid_blue,
                                                     modifier = Modifier.align(Alignment.End),
                                                 )
                                             }
                                         }
                                     }
                                     Row(
                                         modifier = Modifier.padding(20.dp, 0.dp),
                                         horizontalArrangement = Arrangement.SpaceBetween
                                     ) {
                                         // 작성된 리뷰가 없을 경우
                                         if (!paymentDetail.isWriteReview) {
                                             OutlinedButton(
                                                 border = BorderStroke(
                                                     color = Vivid_blue,
                                                     width = 2.dp,
                                                 ),
                                                 modifier = Modifier
                                                     .fillMaxWidth()
                                                     .clip(RoundedCornerShape(10.dp)),
                                                 onClick = { navController.navigate("reviewSave/${paymentDetail.paymentDetailIdx}/0") },
                                                 elevation = ButtonDefaults.elevation(2.dp)
                                             ) {
                                                 Text(
                                                     text = "리뷰 작성",
                                                     color = Main_gray,
                                                 )
                                             }
                                         } else {
                                             OutlinedButton(
                                                 border = BorderStroke(
                                                     color = Vivid_yellow,
                                                     width = 2.dp
                                                 ),
                                                 modifier = Modifier
                                                     .fillMaxWidth()
                                                     .clip(RoundedCornerShape(10.dp)),
                                                 onClick = { navController.navigate("review") },
                                                 elevation = ButtonDefaults.elevation(2.dp)
                                             ) {
                                                 Text(
                                                     text = "리뷰 내역",
                                                     color = Main_gray,
                                                 )
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                     }
                     item() {
                         Column(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .paint(painter = painterResource(R.drawable.receipt_bottom), contentScale = ContentScale.FillWidth)
                         )  {
                             Divider(
                                 thickness = 2.dp,
                                 color = Light_gray,
                                 modifier = Modifier
                                     .padding(30.dp, 10.dp)
                             )
                             Row(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(30.dp, 20.dp),
                                 verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.SpaceBetween,
                             ) {
                                 Text(
                                     text = "총 상품 가격",
                                     fontSize = 16.sp,
                                     fontWeight = FontWeight.Medium,
                                     color = Main_gray,
                                 )
                                 Text(
                                     text = "${DecimalFormat("#,###").format(priceTotal)}원",
                                     fontSize = 18.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = Main_gray,
                                 )
                             }
                             Row(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(30.dp, 15.dp),
                                 verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.SpaceBetween,
                             ) {
                                 Text(
                                     text = "총 할인 금액",
                                     fontSize = 16.sp,
                                     fontWeight = FontWeight.Medium,
                                     color = Vivid_blue,
                                 )
                                 Text(
                                     text = "${DecimalFormat("#,###").format(priceTotal - payment!!.total)}원",
                                     fontSize = 18.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = Vivid_blue,
                                 )
                             }
                             Divider(
                                 thickness = 2.dp,
                                 color = Light_gray,
                                 modifier = Modifier
                                     .padding(horizontal = 30.dp)
                             )
                             Row(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(30.dp, 15.dp),
                                 verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.SpaceBetween,
                             ) {
                                 Text(
                                     text = "총 결제 금액",
                                     fontSize = 18.sp,
                                     fontWeight = FontWeight.Medium,
                                     color = Dark_gray,
                                 )
                                 Text(
                                     text = "${DecimalFormat("#,###").format(payment!!.total)}원",
                                     fontSize = 20.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = Dark_gray,
                                 )
                             }
                         }
                     }
                 }
                 floatingBtns(
                     listState = listState,
                     secondBtn = R.drawable.mypage,
                     secondBtnName = "MYPAGE",
                     secondEvent = {
                         navController.popBackStack(route = "myPage", inclusive = false)
                     }
                 )
             }
         }
    }
    // 이미지 로딩 중일 때
    if(isLoading){
        loadingView()
    }
}