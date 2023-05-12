package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/categories")
class CategoryController @Autowired constructor(
    val categoryService: CategoryService,
) {
    @GetMapping
    fun getCategories(): ResultResponse<List<com.ssafy.mmart.domain.category.dto.CategoryRes>?> {
        return ResultResponse.success(categoryService.getCategories())
    }

}