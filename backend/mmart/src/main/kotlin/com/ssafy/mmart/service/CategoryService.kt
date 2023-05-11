package com.ssafy.mmart.service

import com.ssafy.mmart.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService @Autowired constructor(
    val categoryRepository: CategoryRepository,
) {
    fun getCategories(): List<com.ssafy.mmart.domain.category.dto.CategoryRes>? {
        val temp = categoryRepository.findAll()
        val result: MutableList<com.ssafy.mmart.domain.category.dto.CategoryRes> = mutableListOf()
        temp.forEach { category ->
            result.add(com.ssafy.mmart.domain.category.dto.CategoryRes(category.categoryIdx!!,category.categoryName))
        }
        return result
    }
}
