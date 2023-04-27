package com.ssafy.mmart.service

import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.ReviewDuplicateException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService @Autowired constructor(
    val reviewRepository: ReviewRepository,
    val userRepository: UserRepository,
    val paymentDetailRepository: PaymentDetailRepository,
){
    fun getReview(reviewIdx: Int): Review? {
        return reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
    }

    fun getUserReviews(userIdx: Int): List<Review>? {
        userRepository.findByIdOrNull(userIdx) ?: UserNotFoundException()
        var reviews = reviewRepository.findAllByUser_UserIdx(userIdx)
        return if (reviews.isNullOrEmpty()) {
            throw ReviewsNotFoundException()
        } else {
            reviews
        }
    }

    fun getItemReviews(itemIdx: Int): List<Review>? {
        var reviews = reviewRepository.findAllByItem_ItemIdx(itemIdx)
        return if (reviews.isNullOrEmpty()) {
            throw ReviewsNotFoundException()
        } else {
            reviews
        }
    }

    fun createReview(userIdx: Int, paymentDetailIdx: Int, reviewReq: ReviewReq): Review? {
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        var paymentDetail = paymentDetailRepository.findByIdOrNull(paymentDetailIdx) ?: throw PaymentDetailNotFoundException()
        if (paymentDetail.payment.user != user) throw BadAccessException()
        var oldReview = reviewRepository.findByPaymentDetail(paymentDetail)
        return if (oldReview == null) {
            reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user))
        } else {
            throw ReviewDuplicateException()
        }
    }

    @Transactional
    fun updateReview(userIdx: Int, reviewIdx: Int, reviewReq: ReviewReq): Review? {
        var review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return if (user == review.user) {
            review.apply{
                content = reviewReq.content
                star = reviewReq.star
            }
        } else {
            throw BadAccessException()
        }
    }

    @Transactional
    fun deleteReview(reviewIdx: Int, userIdx: Int): Review? {
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        var review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        if (user == review.user) {
            reviewRepository.deleteById(review.reviewIdx!!)
        } else {
            throw BadAccessException()
        }
        return review
    }
}