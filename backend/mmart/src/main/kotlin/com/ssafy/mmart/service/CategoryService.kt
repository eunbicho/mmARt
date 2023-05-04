package com.ssafy.mmart.service

import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService @Autowired constructor(
    val categoryRepository: CategoryRepository,
) {
    fun getCategories(): List<Category>? {
        //유저가 존재하는지 확인
        return categoryRepository.findAll()
    }
}
