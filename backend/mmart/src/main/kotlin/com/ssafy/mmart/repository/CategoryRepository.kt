package com.ssafy.mmart.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.user.QUser
import com.ssafy.mmart.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.annotation.Resource

@Repository
interface CategoryRepository : JpaRepository<Category, Int>