package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.favorite.dto.CreateFavoriteReq
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.FavoriteDuplicateException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.CategoryRepository
import com.ssafy.mmart.repository.FavoriteRepository
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CategoryService @Autowired constructor(
    val categoryRepository: CategoryRepository,
) {
    fun getCategories(): List<Category>? {
        //유저가 존재하는지 확인
        return categoryRepository.findAll();
    }
}
