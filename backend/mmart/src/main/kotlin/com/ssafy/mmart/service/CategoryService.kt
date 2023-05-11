package com.ssafy.mmart.service

import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.category.dto.CategoryRes
import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService @Autowired constructor(
    val categoryRepository: CategoryRepository,
) {
    fun getCategories(): List<CategoryRes>? {
        val temp = categoryRepository.findAll()
        val result: MutableList<CategoryRes> = mutableListOf()
        temp.forEach { category ->
            result.add(CategoryRes(category.categoryIdx!!,category.categoryName))
        }
        return result
    }
}
