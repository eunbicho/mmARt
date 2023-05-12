package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.item.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Int>{
    fun findByBarcode(barcode:String):Item?
}