package com.ssafy.mmart.service

import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.ReviewDuplicateException
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.exception.not_found.ReviewNotFoundException
import com.ssafy.mmart.exception.not_found.ReviewsNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.ItemRepository
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

    fun createReview(reviewReq: ReviewReq): Review? {
        var user = userRepository.findByIdOrNull(reviewReq.userIdx) ?: throw UserNotFoundException()
        var item = itemRepository.findByIdOrNull(reviewReq.itemIdx) ?: throw ItemNotFoundException()
        var oldReview = reviewRepository.findByUserAndItem(user, item)
        return if (oldReview == null) {
            reviewRepository.save(reviewReq.toEntity(item, user))
        } else {
            throw ReviewDuplicateException()
        }
    }

    @Transactional
    fun updateReview(reviewIdx: Int, reviewReq: ReviewReq): Review? {
        var review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
        var user = userRepository.findByIdOrNull(reviewReq.userIdx) ?: throw UserNotFoundException()
        var item = itemRepository.findByIdOrNull(reviewReq.itemIdx) ?: throw ItemNotFoundException()
        return if (user == review.user && item == review.item) {
            review.apply{
                content = reviewReq.content
                star = star
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