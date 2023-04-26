package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.favorite.dto.CreateFavoriteReq
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.service.FavoriteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/favorites")
class FavoriteController @Autowired constructor(
    val favoriteService: FavoriteService,
) {
    @GetMapping("/{userIdx}")
    fun getFavorites(@PathVariable userIdx: Int): ResultResponse<List<Favorite>?> {
        return ResultResponse.success(favoriteService.getFavorites(userIdx))
    }

    @DeleteMapping("/{userIdx}")
    fun deleteFavorite(@PathVariable userIdx: Int, @RequestParam favoriteIdx: Int): ResultResponse<Favorite?> {
        return ResultResponse.success(favoriteService.deleteFavorite(userIdx,favoriteIdx))
    }

    @PostMapping
    fun createFavorite(@RequestBody createFavoriteReq: CreateFavoriteReq): ResultResponse<Favorite?> {
        return ResultResponse.success(favoriteService.createFavorite(createFavoriteReq))
    }

//    @GetMapping("/barcode")
//    fun getItemByBarcode(@RequestParam barcode: String): ResultResponse<Product?> {
//        return ResultResponse.success(productService.getItemByBarcode(barcode))
//    }
//
//    @GetMapping("/categories")
//    fun getItemByCategory(@RequestParam userIdx: Int, categoryIdx: Int): ResultResponse<List<Product?>> {
//        return ResultResponse.success(productService.getItemByCategory(userIdx,categoryIdx))
//    }

}