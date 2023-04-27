package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import com.ssafy.mmart.domain.itemItemCoupon.ItemItemCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemItemCouponRepository : JpaRepository<ItemItemCoupon, Int>{
    fun findByItem_ItemIdx(itemIdx: Int): ItemItemCoupon?
}