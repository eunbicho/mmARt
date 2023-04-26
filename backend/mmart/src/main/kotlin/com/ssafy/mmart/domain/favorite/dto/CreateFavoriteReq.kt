package com.ssafy.mmart.domain.favorite.dto

import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.repository.CategoryRepository
import com.ssafy.mmart.repository.FavoriteRepository
import com.ssafy.mmart.repository.ProductRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

data class CreateFavoriteReq(
    var categoryIdx:Int,
    var productIdx: Int,
    var userIdx: Int,
    )