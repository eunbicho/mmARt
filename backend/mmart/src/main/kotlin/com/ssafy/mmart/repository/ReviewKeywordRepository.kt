package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.reviewKeyword.ReviewKeyword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewKeywordRepository : JpaRepository<ReviewKeyword, Int> {

}