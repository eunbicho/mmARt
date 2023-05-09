package com.example.mmart

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// http request
interface APIS {

    // 카테고리 별 아이템
    @GET("items/categories")
    suspend fun getCategories(@Query("userIdx") userIdx: Int, @Query("categoryIdx") categoryIdx: Int): ItemsResult

    // 바코드 스캔
    @GET("items/barcode")
    suspend fun getItemByBarcode(@Query("barcode") barcode: Long): ItemsResult

    // 장볼구니 조회
    @GET("getcarts/{userId}")
    suspend fun getCartsRead(@Path("userId") userId: Int): CartResult

    // 장봤구니 조회
    @GET("gotcarts/{userId}")
    suspend fun gotCartsRead(@Path("userId") userId: Int): CartResult

    // 마이페이지 조회
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): UserResult

    // 상품 상세 조회
    @GET("items/{itemId}/detail-image")
    suspend fun getItemInfo(@Path("itemId") itemId: Int): ItemDetailResult

    // 상품 별 리뷰 조회
    @GET("reviews/item")
    suspend fun getItemReview(@Query("itemIdx") itemIdx: Int): ReviewResult

    // 리뷰 작성
//    @POST()

    companion object {
        private const val BASE_URL = "http://k8a405.p.ssafy.io:8090/api/v1/"
//        private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

        fun create(): APIS {
            val gson : Gson =   GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}

// Item 검색 관련 result
data class ItemsResult(
    val resultCode : String,
    val result: List<ItemInfo>
)

data class ItemInfo(
    val itemIdx: Int,
    val itemName: String,
    val price: Int,
    val couponPrice: Int,
    val inventory: Int,
    val barcode: String,
    val thumbnail: String,
    val placeInfo: String?,
    val weight: Int,
    val content: String?,
    val quantity: Int
)

//data class CategoryInfo(
//    val categoryIdx: Int,
//    val categoryName: String,
//    val placeInfo: String?,
//    val createTime: List<Int>,
//    val updateTime: List<Int>
//)

// item 상세 조회 관련 Result
data class ItemDetailResult(
    val resultCode : String,
    val result: ItemDetail
)

// 변경 전 임시로
data class ItemDetail(
    val image: String,
    val itemDetail: Any,
    val item: ItemInfo
)

// 카트 관련 Result
data class CartResult(
    val resultCode : String,
    val result: CartContent
)

data class CartContent(
    val itemList: List<ItemInfo>,
    val total: Int,
)

//data class CartedItemList(
//    val itemIdx: Int,
//    val quantity: Int
//)

// 회원 정보 Result
data class UserResult(
    val resultCode : String,
    val result: UserInfo,
)

data class UserInfo(
    val userIdx: Int,
    val name: String,
    val qrcode: String,
)

// 리뷰 Result
data class ReviewResult(
    val resultCode: String,
    val result: List<ReviewDetail>
)

data class ReviewDetail(
    val star: Int,
    val content: String,
    val paymentDetail: PaymentDetail
)

data class PaymentDetail(
    val user: UserInfo,

)