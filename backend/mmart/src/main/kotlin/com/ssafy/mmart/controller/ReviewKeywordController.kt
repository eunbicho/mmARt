package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.domain.reviewKeyword.dto.ReviewKeywordRes
import com.ssafy.mmart.service.ReviewKeywordService
import com.ssafy.mmart.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/reviews/keyword")
class ReviewKeywordController @Autowired constructor(
    val reviewKeywordService: ReviewKeywordService,
){
    @GetMapping("{itemIdx}")
    fun getReviewKeywords(@PathVariable itemIdx: Int): ResultResponse<List<ReviewKeywordRes>?> {
        return ResultResponse.success(reviewKeywordService.getReviewKeywords(itemIdx))
    }

//    @PostMapping
//    fun createReview(@RequestParam userIdx: Int, @RequestParam paymentDetailIdx: Int, @RequestBody reviewReq: ReviewReq): ResultResponse<Review?> {
//        return ResultResponse.success(reviewService.createReview(userIdx, paymentDetailIdx, reviewReq))
//    }

}