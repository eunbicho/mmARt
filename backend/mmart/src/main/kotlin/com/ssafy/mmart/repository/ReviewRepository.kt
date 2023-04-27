package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Int> {
    fun findAllByUser_UserIdx(userIdx: Int): List<Review>?
    fun findAllByItem_ItemIdx(itemIdx: Int): List<Review>?
    fun findByUserAndItem(user: User, item: Item): Review?
}