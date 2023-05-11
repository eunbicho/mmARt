package com.ssafy.mmart.service

import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.domain.review.dto.ReviewRes
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.ReviewDuplicateException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.ReviewRepository
import com.ssafy.mmart.repository.UserRepository
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
    val userService: UserService
){
    fun setReviewRes(review: Review):ReviewRes?{
        return ReviewRes(review.reviewIdx!!, review.content!!,review.star,userService.setUser(review.user)!!)
    }
    fun getReview(reviewIdx: Int): ReviewRes? {
        return setReviewRes(reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException())
    }

    fun getUserReviews(userIdx: Int): List<ReviewRes>? {
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        var list = mutableListOf<ReviewRes>()
        val temp = reviewRepository.findAllByUser_UserIdx(userIdx)
        if (temp != null) {
            temp.forEach { review ->
                list.add(setReviewRes(review)!!)
            }
        }
        return list
    }

    fun getItemReviews(itemIdx: Int): List<ReviewRes>? {
        itemRepository.findById(itemIdx).orElseThrow(::ItemNotFoundException)
        var list = mutableListOf<ReviewRes>()
        val temp = reviewRepository.findAllByItem_ItemIdx(itemIdx)
        if (temp != null) {
            temp.forEach { review ->
                list.add(setReviewRes(review)!!)
            }
        }
        return list
    }

    fun createReview(userIdx: Int, paymentDetailIdx: Int, reviewReq: ReviewReq): ReviewRes? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val paymentDetail = paymentDetailRepository.findByIdOrNull(paymentDetailIdx) ?: throw PaymentDetailNotFoundException()
        if (paymentDetail.payment.user != user) throw BadAccessException()
        val oldReview = reviewRepository.findByPaymentDetail(paymentDetail)
        return if (oldReview == null) {
            setReviewRes(reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user)))
        } else {
            throw ReviewDuplicateException()
        }
    }

    @Transactional
    fun updateReview(userIdx: Int, reviewIdx: Int, reviewReq: ReviewReq): ReviewRes? {
        var review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        if (user == review.user) {
            review.apply{
                content = reviewReq.content
                star = reviewReq.star
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