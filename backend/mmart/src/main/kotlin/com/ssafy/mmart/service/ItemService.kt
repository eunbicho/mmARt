package com.ssafy.mmart.service

import com.querydsl.core.Tuple
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.favoriteCategory.QFavorite.favorite
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.item.QItem.item
import com.ssafy.mmart.domain.itemCoupon.QItemCoupon.itemCoupon
import com.ssafy.mmart.domain.itemDetailImage.ItemDetailImage
import com.ssafy.mmart.domain.itemDetailImage.QItemDetailImage.itemDetailImage
import com.ssafy.mmart.domain.itemItemCoupon.QItemItemCoupon.itemItemCoupon
import com.ssafy.mmart.domain.payment.QPayment.payment
import com.ssafy.mmart.domain.paymentDetail.QPaymentDetail.paymentDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ItemService @Autowired constructor(
    val jpaQueryFactory: JPAQueryFactory,
) {
    //유저의 구매내역에 따라 검색 부분 리스트는 수정해야함(부가)
    fun getItem(itemIdx: Int): Item? {
        return jpaQueryFactory
            .selectFrom(item)
            .where(item.itemIdx.eq(itemIdx))
            .fetchOne()
    }

    fun getItemDetailImage(itemIdx: Int): ItemDetailImage? {
        return jpaQueryFactory
            .selectFrom(itemDetailImage)
            .where(itemDetailImage.item.itemIdx.eq(itemIdx))
            .fetchOne()
    }

    fun getItemByBarcode(barcode: String): Item? {
        return jpaQueryFactory
            .selectFrom(item)
            .where(item.barcode.eq(barcode))
            .fetchOne()
    }

    fun getItemByCategory(userIdx: Int, categoryIdx: Int): List<Item>? {
        var result = jpaQueryFactory.select(item)
            .from(paymentDetail)
            .join(paymentDetail.payment, payment)
            .join(paymentDetail.item, item)
            .where(payment.user.userIdx.eq(userIdx).and(item.category.categoryIdx.eq(categoryIdx)).and(item.inventory.gt(0)))
            .groupBy(item.itemIdx)
            .orderBy(item.itemIdx.count().desc(), payment.date.max().desc())
            .fetch()
        //이제 재고 있는 전체 리스트 add
        var temp = jpaQueryFactory
            .selectFrom(item)
            .where(
                item.notIn(
                    JPAExpressions.select(item)
                        .from(paymentDetail)
                        .join(paymentDetail.payment, payment)
                        .join(paymentDetail.item, item)
                        .where(payment.user.userIdx.eq(userIdx))
                    ).and(item.category.categoryIdx.eq(categoryIdx)).and(item.inventory.gt(0))
                )
            .fetch()
        result.addAll(temp)
        var temp2 = jpaQueryFactory
            .selectFrom(item)
            .where(item.inventory.eq(0))
            .fetch()
        //한번도 안 산 재고 있는 상품 구해서 넣기
        result.addAll(temp2)
        return result
    }

    fun getItemsFrequent(userIdx: Int): List<Item?> {
        return jpaQueryFactory
            .select(item)
            .from(paymentDetail)
            .join(paymentDetail.item, item)
            .join(paymentDetail.payment, payment)
            .where(payment.user.userIdx.eq(userIdx), item.itemIdx.eq(paymentDetail.item.itemIdx))
            .groupBy(item.itemIdx)
            .orderBy(paymentDetail.count().desc(), payment.date.desc())
            .limit(10)
            .fetch()
    }

    fun getItemsRecent(userIdx: Int): List<Item?> {
        return jpaQueryFactory
            .select(item)
            .from(paymentDetail)
            .join(paymentDetail.item, item)
            .join(paymentDetail.payment, payment)
            .where(payment.user.userIdx.eq(userIdx), item.itemIdx.eq(paymentDetail.item.itemIdx))
            .orderBy(payment.date.desc())
            .fetch()
            .distinctBy{ it.itemIdx }
            .take(10)
    }

    fun getItemsFavorite(userIdx: Int): List<Item?> {
        return jpaQueryFactory
            .select(item)
            .from(favorite)
            .join(favorite.item, item)
            .where(favorite.user.userIdx.eq(userIdx), item.itemIdx.eq(favorite.item.itemIdx))
            .orderBy(favorite.updateTime.desc())
            .limit(10)
            .fetch()
    }

    fun getItemsDiscount(): List<Item?>{
        return jpaQueryFactory
            .select(item)
            .from(itemItemCoupon)
            .join(itemItemCoupon.item, item)
            .join(itemItemCoupon.itemCoupon, itemCoupon)
            .where(item.itemIdx.eq(itemItemCoupon.item.itemIdx))
            .orderBy(itemCoupon.couponExpired.asc())
            .limit(10)
            .fetch()
    }
}
