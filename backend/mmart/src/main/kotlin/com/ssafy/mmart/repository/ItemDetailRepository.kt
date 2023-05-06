package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.itemDetail.ItemDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemDetailRepository : JpaRepository<ItemDetail, Int> {
    fun findByItem(item: Item): ItemDetail?
}