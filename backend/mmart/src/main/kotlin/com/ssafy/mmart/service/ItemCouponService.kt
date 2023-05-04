package com.ssafy.mmart.service

import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import com.ssafy.mmart.repository.ItemItemCouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ItemCouponService @Autowired constructor(
    val itemItemCouponRepository: ItemItemCouponRepository,
) {
    fun getCoupon(itemIdx:Int): ItemCoupon? {
        val itemitem = itemItemCouponRepository.findByItem_ItemIdx(itemIdx)
        //유효기간 체크도 해주기
        return if (itemitem != null && itemitem.itemCoupon.couponExpired.isAfter(LocalDateTime.now())) {
            itemitem.itemCoupon
        }else{
            null
        }
    }
}
