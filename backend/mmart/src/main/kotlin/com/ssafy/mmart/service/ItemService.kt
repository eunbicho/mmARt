package com.ssafy.mmart.service

import com.querydsl.core.Tuple
import com.querydsl.jpa.JPAExpressions
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

    fun getItemByCategory(userIdx: Int, categoryIdx: Int): MutableList<Item>? {
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
}
