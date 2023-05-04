package com.ssafy.mmart.service

import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.ReviewDuplicateException
import com.ssafy.mmart.exception.not_found.PaymentDetailNotFoundException
import com.ssafy.mmart.exception.not_found.ReviewNotFoundException
import com.ssafy.mmart.exception.not_found.ReviewsNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
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
    val paymentDetailRepository: PaymentDetailRepository,
){
    fun getReview(reviewIdx: Int): Review? {
        return reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
    }

    fun getUserReviews(userIdx: Int): List<Review>? {
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val reviews = reviewRepository.findAllByUser_UserIdx(userIdx)
        return if (reviews.isNullOrEmpty()) {
            throw ReviewsNotFoundException()
        } else {
            reviews
        }
    }

    fun getItemReviews(itemIdx: Int): List<Review>? {
        val reviews = reviewRepository.findAllByItem_ItemIdx(itemIdx)
        return if (reviews.isNullOrEmpty()) {
            throw ReviewsNotFoundException()
        } else {
            reviews
        }
    }

    fun createReview(userIdx: Int, paymentDetailIdx: Int, reviewReq: ReviewReq): Review? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val paymentDetail = paymentDetailRepository.findByIdOrNull(paymentDetailIdx) ?: throw PaymentDetailNotFoundException()
        if (paymentDetail.payment.user != user) throw BadAccessException()
        val oldReview = reviewRepository.findByPaymentDetail(paymentDetail)
        return if (oldReview == null) {
            reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user))
        } else {
            throw ReviewDuplicateException()
        }
    }

    @Transactional
    fun updateReview(userIdx: Int, reviewIdx: Int, reviewReq: ReviewReq): Review? {
        val review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
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
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        if (user == review.user) {
            reviewRepository.deleteById(review.reviewIdx!!)
        } else {
            throw BadAccessException()
        }
        return review
    }
}