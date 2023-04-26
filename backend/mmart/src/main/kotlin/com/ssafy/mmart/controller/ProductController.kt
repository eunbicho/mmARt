package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/items")
class ProductController @Autowired constructor(
    val productService: ProductService,
) {
    @GetMapping("/{productIdx}")
    fun getItem(@PathVariable productIdx: Int, @RequestParam userIdx: Int): ResultResponse<Product?> {
        return ResultResponse.success(productService.getItem(productIdx, userIdx))
    }

    @GetMapping("/barcode")
    fun getItemByBarcode(@RequestParam barcode: String): ResultResponse<Product?> {
        return ResultResponse.success(productService.getItemByBarcode(barcode))
    }

    @GetMapping("/categories")
    fun getItemByCategory(@RequestParam userIdx: Int, categoryIdx: Int): ResultResponse<List<Product?>> {
        return ResultResponse.success(productService.getItemByCategory(userIdx,categoryIdx))
    }

}