package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.favorite.dto.CreateFavoriteReq
import com.ssafy.mmart.domain.favoriteCategory.Favorite
import com.ssafy.mmart.domain.favoriteCategory.QFavorite.favorite
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.product.QProduct.product
import com.ssafy.mmart.domain.user.dto.CreateUserReq
import com.ssafy.mmart.repository.CategoryRepository
import com.ssafy.mmart.repository.FavoriteRepository
import com.ssafy.mmart.repository.ProductRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class FavoriteService @Autowired constructor(
    val favoriteRepository: FavoriteRepository,
    val userRepository: UserRepository,
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository,
    val jpaQueryFactory: JPAQueryFactory,
) {
    fun getFavorites(userIdx: Int): List<Product?> {
        val favoriteList = jpaQueryFactory.selectFrom(favorite)
            .where(favorite.user.userIdx.eq(userIdx))
            .fetch()
        var resultList = mutableListOf<Product?>()
        for(temp in favoriteList){
            resultList.add(temp.product)
        }
        return resultList
    }
    @Transactional
    fun deleteFavorite(userIdx: Int, favoriteIdx:Int): Favorite? {
        //조건에 맞는 즐겨찾기가 있는지 확인
        val result = jpaQueryFactory.selectFrom(favorite).where(favorite.favoriteIdx.eq(favoriteIdx).and(favorite.user.userIdx.eq(userIdx))).fetchOne()
        jpaQueryFactory.delete(favorite).where(favorite.favoriteIdx.eq(userIdx).and(favorite.user.userIdx.eq(userIdx))).execute()
        return result
    }

    @Transactional
    fun createFavorite(createFavoriteReq: CreateFavoriteReq): Favorite? {
        //조건에 맞는 즐겨찾기가 있는지 확인
//        if(jpaQueryFactory.selectFrom(favorite).where(favorite.product.productIdx.eq(createFavoriteReq.productIdx).and(favorite.user.userIdx.eq(createFavoriteReq.userIdx)))){
//
//        }
        val result = Favorite(user = userRepository.findById(createFavoriteReq.userIdx).get(),
            category = categoryRepository.findById(createFavoriteReq.categoryIdx).get(),
            product = productRepository.findById(createFavoriteReq.productIdx).get())
        print(result)
        return favoriteRepository.save(result)
    }
}
