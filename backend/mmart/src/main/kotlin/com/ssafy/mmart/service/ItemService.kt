package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.item.QItem.item
import com.ssafy.mmart.domain.payment.QPayment.payment
import com.ssafy.mmart.domain.paymentDetail.QPaymentDetail.paymentDetail
import com.ssafy.mmart.repository.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemService @Autowired constructor(
    val itemRepository : ItemRepository,
    val jpaQueryFactory: JPAQueryFactory,
) {
    //유저의 구매내역에 따라 검색 부분 리스트는 수정해야함(부가)
    fun getItem(itemIdx: Int): Item? {
        return jpaQueryFactory
            .selectFrom(item)
            .where(item.itemIdx.eq(itemIdx))
            .fetchOne()
    }

    fun getItemByBarcode(barcode: String): Item? {
        return jpaQueryFactory
            .selectFrom(item)
            .where(item.barcode.eq(barcode))
            .fetchOne()
    }

    fun getItemByCategory(userIdx: Int, categoryIdx: Int): List<Item?> {
        return jpaQueryFactory
            .selectFrom(item)
            .where(item.category.categoryIdx.eq(categoryIdx))
            .fetch()
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
}
