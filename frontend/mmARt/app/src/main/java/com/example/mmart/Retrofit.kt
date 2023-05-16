package com.example.mmart

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// http request
interface APIS {

    // 로그인
    @POST("users/login")
    suspend fun login(@Body body: Any): UserResult

    // 회원가입
    @POST("users")
    suspend fun signUp(@Body body: Any): UserResult

    // 중복확인
    @GET("users")
    suspend fun duplicationCheck(@Query("email") email: String): UserResult

    // 최근 구매
    @GET("items/recent")
    suspend fun getRecentItems(@Query("userIdx") userIdx: Int): ItemsResult

    // 자주 구매
    @GET("items/frequent")
    suspend fun getFrequentItems(@Query("userIdx") userIdx: Int): ItemsResult

    // 검색 결과
    @GET("items/search")
    suspend fun getSearchResult(@Query("keyword") keyword: String): ItemsResult

    // 카테고리 별 아이템
    @GET("items/categories")
    suspend fun getCategories(@Query("userIdx") userIdx: Int, @Query("categoryIdx") categoryIdx: Int): ItemsResult

    // 바코드 스캔
    @GET("items/barcode")
    suspend fun getItemByBarcode(@Query("barcode") barcode: Long): ItemsResult

    // 장볼구니 조회
    @GET("getcarts/{userIdx}")
    suspend fun getGetCarts(@Path("userIdx") userIdx: Int): CartResult

    // 장볼구니 수정
    @PUT("getcarts")
    suspend fun updateGetCart(@Body cartReq: Any): CartResult

    // 장볼구니에서 아이템 삭제
    @DELETE("getcarts")
    suspend fun deleteGetCart(@Query("userIdx") userIdx: Int, @Query("itemIdx") itemIdx: Int)

    // 장봤구니 조회
    @GET("gotcarts/{userIdx}")
    suspend fun getGotCarts(@Path("userIdx") userIdx: Int): CartResult

    // 장봤구니 수정
    @PUT("gotcarts")
    suspend fun updateGotCart(@Body cartReq: Any): CartResult

    // 장봤구니에서 아이템 삭제
    @DELETE("gotcarts")
    suspend fun deleteGotCart(@Query("userIdx") userIdx: Int, @Query("itemIdx") itemIdx: Int)

    // 상품 상세 조회
    @GET("items/{itemIdx}")
    suspend fun getItemInfo(@Path("itemIdx") itemIdx: Int): ItemDetailResult

    // 장볼구니 추가
    @POST("getcarts")
    suspend fun addGetCart(@Body body: Any): CartResult

    // 상품 별 리뷰 조회
    @GET("reviews/item")
    suspend fun getItemReview(@Query("itemIdx") itemIdx: Int): IdxReviewsResult

    // 마이페이지 조회
    @GET("users/{userIdx}")
    suspend fun getUser(@Path("userIdx") userIdx: Int): UserResult

    // 결제 내역 전체 조회
    @GET("payments")
    suspend fun getPayments(@Query("userIdx") userIdx: Int): PaymentsResult

    // 결제 내역 개별 조회
    @GET("payments/{paymentIdx}")
    suspend fun getPayment(@Path("paymentIdx") paymentIdx: Int, @Query("userIdx") userIdx: Int): PaymentResult


    // 결제 내역 상세 조회
    @GET("payments/detail")
    suspend fun getPaymentDetails(@Query("userIdx") userIdx: Int, @Query("paymentIdx") paymentIdx: Int): PaymentDetailsResult

    // 결제 내역 개별 상세 조회 (리뷰 작성용)
    @GET("payments/detail/{paymentDetailIdx}")
    suspend fun getPaymentDetail(@Path("paymentDetailIdx") paymentDetailIdx: Int): PaymentDetailResult

    // 리뷰 작성
    @POST("reviews")
    suspend fun createReview(@Query("userIdx") userIdx: Int, @Query("paymentDetailIdx") paymentIdx: Int, @Body body: Any): ReviewResult

    // 유저 별 리뷰 조회
    @GET("reviews/user")
    suspend fun getUserReview(@Query("userIdx") userIdx: Int): ReviewsResult

    // 리뷰 개별 조회 (리뷰 수정용)
    @GET("reviews")
    suspend fun getReview(@Query("reviewIdx") reviewIdx: Int): ReviewResult

    // 리뷰 수정
    @PUT("reviews")
    suspend fun updateReview(@Query("userIdx") userIdx: Int, @Query("reviewIdx") reviewIdx: Int, @Body body: Any): ReviewResult
    // 리뷰 삭제
    @DELETE("reviews")
    suspend fun deleteReview(@Query("userIdx") userIdx: Int, @Query("reviewIdx") reviewIdx: Int): ReviewResult

    companion object {
        private const val BASE_URL = "http://k8a405.p.ssafy.io:8090/api/v1/"
//        private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

        fun create(): APIS {
            val gson : Gson = GsonBuilder().setLenient().create();
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
    val isCoupon: Boolean,
    val couponPrice: Int,
    val inventory: Int,
    val barcode: String,
    val thumbnail: String,
    val placeInfo: String?,
    val weight: Int,
    val content: String?,
    val quantity: Int,
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
    val result: ItemInfo
)

// 카트 관련 Result
data class CartResult(
    val resultCode : String,
    val result: CartContent
)

data class CartContent(
    val itemList: List<ItemInfo>,
    val priceTotal: Int,
    val discountTotal: Int,
    val total: Int,
)

//data class CartedItemList(
//    val itemIdx: Int,
//    val quantity: Int
//)

// 회원 정보 Result
data class UserResult(
    val resultCode : String,
    val result: UserInfo
)

data class UserInfo(
    val userIdx: Int,
    val name: String,
    val qrcode: String,
)

// 아이템 별 리뷰 조회
data class IdxReviewsResult(
    val resultCode: String,
    val result: ReviewsPos
)

data class ReviewsPos(
    val reviewRes: List<ReviewDetail>,
    val pos: Int
)

// 유저 별 리뷰 조회
data class ReviewsResult(
    val resultCode: String,
    val result: List<ReviewDetail>
)

// 리뷰 개별 조회
data class ReviewResult(
    val resultCode: String,
    val result: ReviewDetail
)

data class ReviewDetail(
    val reviewIdx: Int,
    val star: Int,
    val content: String,
    val paymentDetail: PaymentDetail,
    val user: UserInfo,
    val item: ItemInfo,
    val date: String
)

// 리뷰 body (작성, 수정용)
data class ReviewBody(
    var star: Int,
    var content: String
)

data class PaymentDetail(
    val paymentDetailIdx: Int,
    val quantity: Int,
    val discount: Int,
    val totalPrice: Int,
    val createTime: List<Int>,
    val item: ItemInfo,
    val isWriteReview: Boolean
)

// payments
data class PaymentsResult(
    val resultCode: String,
    val result: List<Payment>
)

data class PaymentResult(
    val resultCode: String,
    val result: Payment
)

data class Payment(
    val paymentIdx: Int,
    val marketName: String,
    val date: List<Int>,
    val total: Int
)

// 결제 내역 상세 조회
data class PaymentDetailsResult(
    val result: List<PaymentDetail>
)

// 결제 내역 개별 상세 조회 (리뷰 작성용)
data class PaymentDetailResult(
    val result: PaymentDetail
)
