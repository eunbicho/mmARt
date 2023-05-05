package com.ssafy.mmart.service

import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.reviewKeyword.QReviewKeyword.reviewKeyword
import com.ssafy.mmart.domain.reviewKeyword.ReviewKeyword
import com.ssafy.mmart.domain.reviewKeyword.dto.ReviewKeywordReq
import com.ssafy.mmart.domain.reviewKeyword.dto.ReviewKeywordRes
import com.ssafy.mmart.domain.reviewReviewKeyword.QReviewReviewKeyword.reviewReviewKeyword
import com.ssafy.mmart.domain.reviewReviewKeyword.ReviewReviewKeyword
import com.ssafy.mmart.exception.not_found.ReviewNotFoundException
import com.ssafy.mmart.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ReviewKeywordService @Autowired constructor(
    val jpaQueryFactory: JPAQueryFactory,
    val reviewKeywordRepository: ReviewKeywordRepository,
    val reviewReviewKeywordRepository: ReviewReviewKeywordRepository,
    val userRepository: UserRepository,
    val reviewRepository: ReviewRepository,
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
    fun createReviewKeyword(reviewKeywordReq: ReviewKeywordReq): List<ReviewKeywordRes>? {
        //같은 키워드가 이미 있는지 확인
        var temp = reviewKeywordRepository.findByKeywordContent(reviewKeywordReq.keywordContent)
        val review = reviewRepository.findById(reviewKeywordReq.reviewIdx).orElseThrow(::ReviewNotFoundException)
        if(temp == null){
            //같은 키워드가 없다면, reviewKeyword에 추가해주자.
            temp = reviewKeywordRepository.save(ReviewKeyword(isPositive = reviewKeywordReq.isPositive, keywordContent = reviewKeywordReq.keywordContent))
        }
        //review-reivewKeyword 테이블에 추가해주기
        reviewReviewKeywordRepository.save(ReviewReviewKeyword(reviewKeyword = temp, review = review, item = review.item))
        return getReviewKeywords(review.item.itemIdx!!)
    }
}