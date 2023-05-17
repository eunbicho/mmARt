package com.ssafy.mmart.service

import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ItemReviewRes
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.domain.review.dto.ReviewRes
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.ReviewDuplicateException
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.exception.not_found.PaymentDetailNotFoundException
import com.ssafy.mmart.exception.not_found.ReviewNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.ReviewRepository
import com.ssafy.mmart.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService @Autowired constructor(
    val reviewRepository: ReviewRepository,
    val userRepository: UserRepository,
    val itemRepository: ItemRepository,
    val paymentDetailRepository: PaymentDetailRepository,
    val userService: UserService,
    val itemService: ItemService
) {
    fun setReviewRes(review: Review): ReviewRes? {
        return ReviewRes(
            review.reviewIdx!!,
            review.content!!,
            review.star,
            userService.setUser(review.user)!!,
            itemService.setItemRes(review.item)!!,
            review.createTime.toString(),
            review.isPositive
        )
    }

    fun getReview(reviewIdx: Int): ReviewRes? {
        return setReviewRes(reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException())
    }

    fun getUserReviews(userIdx: Int): List<ReviewRes>? {
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val list = mutableListOf<ReviewRes>()
        reviewRepository.findAllByUser_UserIdxOrderByCreateTimeDesc(userIdx)?.forEach { review ->
            list.add(setReviewRes(review)!!)
        }
        return list
    }

    fun getItemReviews(itemIdx: Int): ItemReviewRes? {
        itemRepository.findById(itemIdx).orElseThrow(::ItemNotFoundException)
        val list = mutableListOf<ReviewRes>()
        var total = 0
        var positive = 0

        reviewRepository.findAllByItem_ItemIdxOrderByCreateTimeDesc(itemIdx)?.forEach { review ->
            total++
            list.add(setReviewRes(review)!!)
            if (review.isPositive)
                positive++

        }
        return ItemReviewRes(reviewRes = list, pos = if (total != 0) 100.0 * positive / total else -1.0)
    }

    fun createReview(userIdx: Int, paymentDetailIdx: Int, reviewReq: ReviewReq): ReviewRes? {
        val url = "http://k8a405.p.ssafy.io:8788/reviews/sentiment-analysis"  // 요청을 보낼 URL
        // 요청에 필요한 데이터를 객체에 담습니다.
        val requestBody = """
        {
            "review": "${reviewReq.content}"
        }
    """.trimIndent()
        println(reviewReq.content)

        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val paymentDetail =
            paymentDetailRepository.findByIdOrNull(paymentDetailIdx) ?: throw PaymentDetailNotFoundException()
        if (paymentDetail.payment.user != user) throw BadAccessException()
        val oldReview = reviewRepository.findByPaymentDetail(paymentDetail)
        return if (oldReview == null) {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaType()
            val request = Request.Builder()
                .url(url)
                .post(requestBody.toRequestBody(mediaType))
                .build()

            val response = client.newCall(request).execute()
            if ("0" == response.body?.string()) {
                //pos 값을 false로 넣기!!!
                response.close()
                setReviewRes(reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user, false)))
            } else {
                response.close()
                setReviewRes(reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user, true)))
            }

        } else {
            throw ReviewDuplicateException()
        }
    }

    @Transactional
    fun updateReview(userIdx: Int, reviewIdx: Int, reviewReq: ReviewReq): ReviewRes? {
        val url = "http://k8a405.p.ssafy.io:8788/reviews/sentiment-analysis"  // 요청을 보낼 URL
        // 요청에 필요한 데이터를 객체에 담습니다.
        val requestBody = """
        {
            "review": "${reviewReq.content}"
        }
    """.trimIndent()
        var review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        if (user == review.user) {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaType()
            val request = Request.Builder()
                .url(url)
                .post(requestBody.toRequestBody(mediaType))
                .build()

            val response = client.newCall(request).execute()
            val pos = response.body?.string() != "0"
            response.close()
            review.apply {
                content = reviewReq.content
                star = reviewReq.star
                isPositive = pos
            }
        } else {
            throw BadAccessException()
        }
        review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        return setReviewRes(review)
    }

    @Transactional
    fun deleteReview(reviewIdx: Int, userIdx: Int): ReviewRes? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        if (user == review.user) {
            reviewRepository.deleteById(review.reviewIdx!!)
        } else {
            throw BadAccessException()
        }
        return setReviewRes(review)
    }
}