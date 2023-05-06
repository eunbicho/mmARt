package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ItemCouponRepository : JpaRepository<ItemCoupon, Int>{
}