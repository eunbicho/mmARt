package com.ssafy.mmart.service

import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.reviewKeyword.QReviewKeyword.reviewKeyword
import com.ssafy.mmart.domain.reviewKeyword.dto.ReviewKeywordRes
import com.ssafy.mmart.domain.reviewReviewKeyword.QReviewReviewKeyword.reviewReviewKeyword
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.ReviewKeywordRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ReviewKeywordService @Autowired constructor(
    val jpaQueryFactory: JPAQueryFactory,
    val reviewKeywordRepository: ReviewKeywordRepository,
    val userRepository: UserRepository,
    val paymentDetailRepository: PaymentDetailRepository,
){

    fun getReviewKeywords(itemIdx: Int): List<ReviewKeywordRes>? {
        var result = jpaQueryFactory
            .select(reviewKeyword.isPositive,reviewKeyword.keywordContent,
                Expressions.`as`(reviewReviewKeyword.reviewReviewKeywordIdx.count(),"cnt"))
            .from(reviewReviewKeyword)
            .join(reviewReviewKeyword.reviewKeyword, reviewKeyword)
            .where(reviewReviewKeyword.item.itemIdx.eq(itemIdx))
            .groupBy(reviewKeyword.reviewKeywordIdx)
            .orderBy(reviewReviewKeyword.reviewReviewKeywordIdx.count().desc())
            .fetch()
        var reviewKeywordRes:MutableList<ReviewKeywordRes> = mutableListOf()
        if(result.isNotEmpty()){
            result.forEach { node->
                var newNode = node.toArray()
                print("${node::class.simpleName}")

                reviewKeywordRes.add(ReviewKeywordRes(isPositive = newNode[0] as Boolean, keywordContent = newNode[1] as String, cnt = Integer.parseInt(
                    newNode[2].toString()
                )))
            }
        }
//        var result = jpaQueryFactory
//            .select(reviewReviewKeyword.reviewKeyword.keywordContent, Expressions.`as`(count(reviewReviewKeyword.reviewReviewKeywordIdx),"cnt"))
//            .from(reviewReviewKeyword)
//            .join(reviewReviewKeyword.reviewKeyword,reviewKeyword)
//            .where(reviewReviewKeyword.item.itemIdx.eq(itemIdx))
//            .groupBy(reviewReviewKeyword.reviewKeyword)
//            .orderBy(count(reviewReviewKeyword.reviewReviewKeywordIdx).asc)
//            .fetch()
        return reviewKeywordRes;
    }
//
//    fun getUserReviews(userIdx: Int): List<Review>? {
//        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
//        val reviews = reviewRepository.findAllByUser_UserIdx(userIdx)
//        return if (reviews.isNullOrEmpty()) {
//            throw ReviewsNotFoundException()
//        } else {
//            reviews
//        }
//    }
//
//    fun getItemReviews(itemIdx: Int): List<Review>? {
//        val reviews = reviewRepository.findAllByItem_ItemIdx(itemIdx)
//        return if (reviews.isNullOrEmpty()) {
//            throw ReviewsNotFoundException()
//        } else {
//            reviews
//        }
//    }
//
//    fun createReview(userIdx: Int, paymentDetailIdx: Int, reviewReq: ReviewReq): Review? {
//        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
//        val paymentDetail = paymentDetailRepository.findByIdOrNull(paymentDetailIdx) ?: throw PaymentDetailNotFoundException()
//        if (paymentDetail.payment.user != user) throw BadAccessException()
//        val oldReview = reviewRepository.findByPaymentDetail(paymentDetail)
//        return if (oldReview == null) {
//            reviewRepository.save(reviewReq.toEntity(paymentDetail.item, paymentDetail, user))
//        } else {
//            throw ReviewDuplicateException()
//        }
//    }
//
//    @Transactional
//    fun updateReview(userIdx: Int, reviewIdx: Int, reviewReq: ReviewReq): Review? {
//        val review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
//        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
//        return if (user == review.user) {
//            review.apply{
//                content = reviewReq.content
//                star = reviewReq.star
//            }
//        } else {
//            throw BadAccessException()
//        }
//    }
//
//    @Transactional
//    fun deleteReview(reviewIdx: Int, userIdx: Int): Review? {
//        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
//        val review = reviewRepository.findByIdOrNull(reviewIdx) ?: throw ReviewNotFoundException()
//        if (user == review.user) {
//            reviewRepository.deleteById(review.reviewIdx!!)
//        } else {
//            throw BadAccessException()
//        }
//        return review
//    }
}