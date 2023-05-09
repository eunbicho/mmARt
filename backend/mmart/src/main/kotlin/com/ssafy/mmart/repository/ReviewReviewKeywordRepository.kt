package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.reviewKeyword.ReviewKeyword
import com.ssafy.mmart.domain.reviewReviewKeyword.ReviewReviewKeyword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewReviewKeywordRepository : JpaRepository<ReviewReviewKeyword, Int>