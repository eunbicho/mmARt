package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.favorite.dto.CreateFavoriteReq
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.service.CategoryService
import com.ssafy.mmart.service.FavoriteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/categories")
class CategoryController @Autowired constructor(
    val categoryService: CategoryService,
) {
    @GetMapping
    fun getCategories(): ResultResponse<List<Category>?> {
        return ResultResponse.success(categoryService.getCategories())
    }

}