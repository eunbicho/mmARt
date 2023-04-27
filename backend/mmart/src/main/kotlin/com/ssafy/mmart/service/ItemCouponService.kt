package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.favorite.dto.CreateFavoriteReq
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.FavoriteDuplicateException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class ItemCouponService @Autowired constructor(
    val itemCouponRepository: ItemCouponRepository,
    val itemItemCouponRepository: ItemItemCouponRepository,
) {
    fun getCoupon(itemIdx:Int): ItemCoupon? {
        val itemitem = itemItemCouponRepository.findByItem_ItemIdx(itemIdx)
        //유효기간 체크도 해주기
        if (itemitem != null && itemitem.itemCoupon.couponExpired.isAfter(LocalDateTime.now())) {
            return itemitem.itemCoupon
        }else{
            return null
        }


    }
}
