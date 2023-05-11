package com.ssafy.mmart.service

import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import com.ssafy.mmart.domain.itemCoupon.dto.ItemCouponRes
import com.ssafy.mmart.repository.ItemItemCouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ItemCouponService @Autowired constructor(
    val itemItemCouponRepository: ItemItemCouponRepository,
) {
    fun getCoupon(itemIdx:Int): ItemCouponRes? {
        val itemitem = itemItemCouponRepository.findByItem_ItemIdx(itemIdx)
        //유효기간 체크도 해주기
        return if (itemitem != null && itemitem.itemCoupon.couponExpired.isAfter(LocalDateTime.now())) {
            ItemCouponRes(itemitem.itemCoupon.itemCouponIdx!!,itemitem.itemCoupon.couponName,itemitem.itemCoupon.couponDiscount,itemitem.itemCoupon.couponExpired)
        }else{
            null
        }
    }
}
