package com.ssafy.mmart.service

import com.google.gson.Gson
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.favoriteCategory.QFavorite.favorite
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.item.QItem
import com.ssafy.mmart.domain.item.QItem.item
import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.itemCoupon.QItemCoupon.itemCoupon
import com.ssafy.mmart.domain.itemDetailImage.QItemDetailImage.itemDetailImage
import com.ssafy.mmart.domain.itemDetailImage.dto.GetItemImageDetailRes
import com.ssafy.mmart.domain.itemItemCoupon.QItemItemCoupon.itemItemCoupon
import com.ssafy.mmart.domain.payment.QPayment.payment
import com.ssafy.mmart.domain.paymentDetail.QPaymentDetail.paymentDetail
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.repository.ItemCouponRepository
import com.ssafy.mmart.repository.ItemDetailRepository
import com.ssafy.mmart.repository.ItemRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime


@Service
class ItemService @Autowired constructor(
    val jpaQueryFactory: JPAQueryFactory,
    val itemDetailRepository: ItemDetailRepository,
    val couponRepository: ItemCouponRepository,
    val itemRepository: ItemRepository,
) {
    //유저의 구매내역에 따라 검색 부분 리스트는 수정해야함(부가)
    fun setItemRes(item: Item): GetItemRes? {
        val detail = itemDetailRepository.findByItem(item)!!
        var eachPrice = item.price
        var isCoupon = false
        //쿠폰이 있으면, 쿠폰 가격만큼 item의 price에서 빼준다.
        val itemItemCoupon = jpaQueryFactory
            .selectFrom(itemItemCoupon)
            .join(itemItemCoupon.item, QItem.item)
            .join(itemItemCoupon.itemCoupon, itemCoupon)
            .where(itemItemCoupon.item.eq(item), itemCoupon.couponExpired.after(LocalDateTime.now()))
            .orderBy(itemCoupon.couponDiscount.desc())
            .fetchOne()
        if (itemItemCoupon != null) {
            isCoupon = true
            val itemCoupon = couponRepository.findById(itemItemCoupon.itemCoupon.itemCouponIdx!!)
            eachPrice -= itemCoupon.get().couponDiscount
        }
        return GetItemRes(
            itemIdx = item.itemIdx,
            itemName = item.itemName,
            price = item.price,
            isCoupon = isCoupon,
            couponPrice=eachPrice,
            inventory = item.inventory,
            barcode = item.barcode,
            thumbnail = item.thumbnail,
            placeInfo = item.placeInfo,
            weight = item.weight,
            content = detail.content,
        )
    }

    fun setItemListRes(items: List<Item>): List<GetItemRes>? {
        val result: MutableList<GetItemRes> = mutableListOf()
        items.forEach { item ->
            result.add(setItemRes(item)!!)
        }
        return result
    }

    fun getItem(itemIdx: Int): GetItemRes? {
        return setItemRes(
            jpaQueryFactory
                .selectFrom(item)
                .where(item.itemIdx.eq(itemIdx))
                .fetchOne()!!
        )
    }

    fun getItemDetailImage(itemIdx: Int): GetItemImageDetailRes? {
        val result = jpaQueryFactory
            .selectFrom(itemDetailImage)
            .where(itemDetailImage.item.itemIdx.eq(itemIdx))
            .fetchOne()
        if (result != null) {
            return GetItemImageDetailRes(result.itemDetailImageIdx!!, result.image)
        }
        return null
    }

    fun getItemByBarcode(barcode: String): GetItemRes? {
        val result = itemRepository.findByBarcode(barcode)?: throw ItemNotFoundException()
        return setItemRes(
            result
        )
    }

    fun getItemByCategory(userIdx: Int, categoryIdx: Int): List<GetItemRes>? {
        val result = jpaQueryFactory.select(item)
            .from(paymentDetail)
            .join(paymentDetail.payment, payment)
            .join(paymentDetail.item, item)
            .where(
                payment.user.userIdx.eq(userIdx).and(item.category.categoryIdx.eq(categoryIdx))
                    .and(item.inventory.gt(0))
            )
            .groupBy(item.itemIdx)
            .orderBy(item.itemIdx.count().desc(), payment.date.max().desc())
            .fetch()
        //이제 재고 있는 전체 리스트 add
        val temp = jpaQueryFactory
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
        val temp2 = jpaQueryFactory
            .selectFrom(item)
            .where(item.inventory.eq(0),item.category.categoryIdx.eq(categoryIdx))
            .fetch()
        //한번도 안 산 재고 있는 상품 구해서 넣기
        result.addAll(temp2)
        return setItemListRes(result)
    }
    fun getItemBySearch(keyword : String): List<GetItemRes>? {
        val result: MutableList<Item> = mutableListOf()
        val url = "http://k8a405.p.ssafy.io:8200/_search"  // 요청을 보낼 URL
        // 요청에 필요한 데이터를 객체에 담습니다.


        val requestBody = """
        {
            "query": {
                "match": {
                    "item_name.nori": "$keyword"
                }
            }
        }
    """.trimIndent()

        val client = OkHttpClient()
        val mediaType = "application/json".toMediaType()
        val request = Request.Builder()
            .url(url)
            .post(requestBody.toRequestBody(mediaType))
            .build()

        val response = client.newCall(request).execute()
        val gson = Gson()
        val searchResult = gson.fromJson(response.body?.string(), SearchResult::class.java)

//        // 파싱된 객체 사용 예시
//        println("Took: ${searchResult.took}")
//        println("Timed Out: ${searchResult.timed_out}")
//        println("Total Hits: ${searchResult.hits.total.value}")

        // hits에 포함된 아이템 출력
        searchResult.hits.hits.forEach { hit ->
            result.add(itemRepository.findById(hit._source.item_idx).get())
        }
        response.close()
        return setItemListRes(result)
    }

    fun getItemsFrequent(userIdx: Int): List<GetItemRes>? {
        return setItemListRes(jpaQueryFactory
            .select(item)
            .from(paymentDetail)
            .join(paymentDetail.item, item)
            .join(paymentDetail.payment, payment)
            .where(payment.user.userIdx.eq(userIdx), item.itemIdx.eq(paymentDetail.item.itemIdx))
            .groupBy(item.itemIdx)
            .orderBy(paymentDetail.count().desc(), payment.date.max().desc())
            .limit(10)
            .fetch())
    }

    fun getItemsRecent(userIdx: Int): List<GetItemRes>? {
        return setItemListRes(jpaQueryFactory
            .select(item)
            .from(paymentDetail)
            .join(paymentDetail.item, item)
            .join(paymentDetail.payment, payment)
            .where(payment.user.userIdx.eq(userIdx), item.itemIdx.eq(paymentDetail.item.itemIdx))
            .orderBy(payment.date.desc())
            .fetch()
            .distinctBy { it.itemIdx }
            .take(10))
    }

    fun getItemsFavorite(userIdx: Int): List<GetItemRes>? {
        return setItemListRes(jpaQueryFactory
            .select(item)
            .from(favorite)
            .join(favorite.item, item)
            .where(favorite.user.userIdx.eq(userIdx), item.itemIdx.eq(favorite.item.itemIdx))
            .orderBy(favorite.updateTime.desc())
            .limit(10)
            .fetch())
    }

    fun getItemsDiscount(): List<GetItemRes>? {
        return setItemListRes(jpaQueryFactory
            .select(item)
            .from(itemItemCoupon)
            .join(itemItemCoupon.item, item)
            .join(itemItemCoupon.itemCoupon, itemCoupon)
            .where(item.itemIdx.eq(itemItemCoupon.item.itemIdx),itemCoupon.couponExpired.after(LocalDateTime.now()))
            .orderBy(itemCoupon.couponExpired.asc())
            .limit(10)
            .fetch())
    }

}

data class SearchResult(
    val took: Int,
    val timed_out: Boolean,
    val _shards: Shards,
    val hits: Hits
)

data class Shards(
    val total: Int,
    val successful: Int,
    val skipped: Int,
    val failed: Int
)

data class Hits(
    val total: Total,
    val max_score: Double,
    val hits: List<Hit>
)

data class Total(
    val value: Int,
    val relation: String
)

data class Hit(
    val _index: String,
    val _type: String,
    val _id: String,
    val _score: Double,
    val _source: Itemss
)

data class Itemss(
    val item_idx: Int,
    val item_name: String,
    val inventory: String,
    val price: String,
    val thumbnail: String
)
