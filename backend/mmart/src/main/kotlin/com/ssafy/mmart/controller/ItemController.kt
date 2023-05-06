package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.itemDetailImage.ItemDetailImage
import com.ssafy.mmart.domain.itemDetailImage.dto.GetItemImageDetailRes
import com.ssafy.mmart.service.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/items")
class ItemController @Autowired constructor(
    val itemService: ItemService,
) {
    @GetMapping("/{itemIdx}")
    fun getItem(@PathVariable itemIdx: Int): ResultResponse<GetItemRes?> {
        return ResultResponse.success(itemService.getItem(itemIdx))
    }
    @GetMapping("/{itemIdx}/detail-image")
    fun getItemDetailImage(@PathVariable itemIdx: Int): ResultResponse<GetItemImageDetailRes?> {
        return ResultResponse.success(itemService.getItemDetailImage(itemIdx))
    }

    @GetMapping("/barcode")
    fun getItemByBarcode(@RequestParam barcode: String): ResultResponse<GetItemRes?> {
        return ResultResponse.success(itemService.getItemByBarcode(barcode))
    }

    @GetMapping("/categories")
    fun getItemByCategory(@RequestParam userIdx: Int, categoryIdx: Int): ResultResponse<List<GetItemRes>?> {
        return ResultResponse.success(itemService.getItemByCategory(userIdx,categoryIdx))
    }

    @GetMapping("/frequent")
    fun getItemsFrequent(@RequestParam userIdx: Int): ResultResponse<List<GetItemRes>?> {
        return ResultResponse.success(itemService.getItemsFrequent(userIdx))
    }

    @GetMapping("/recent")
    fun getItemsRecent(@RequestParam userIdx: Int): ResultResponse<List<GetItemRes>?> {
        return ResultResponse.success(itemService.getItemsRecent(userIdx))
    }

    @GetMapping("/favorite")
    fun getItemsFavorite(@RequestParam userIdx: Int): ResultResponse<List<GetItemRes>?> {
        return ResultResponse.success(itemService.getItemsFavorite(userIdx))
    }

    @GetMapping("/discount")
    fun getItemsDiscount(): ResultResponse<List<GetItemRes>?> {
        return ResultResponse.success(itemService.getItemsDiscount())
    }
}