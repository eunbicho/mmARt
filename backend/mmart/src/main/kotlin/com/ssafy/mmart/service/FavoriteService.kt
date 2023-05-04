package com.ssafy.mmart.service

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
class FavoriteService @Autowired constructor(
    val favoriteRepository: FavoriteRepository,
    val userRepository: UserRepository,
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository,
) {
    fun getFavorites(userIdx: Int): List<Favorite>? {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        return favoriteRepository.findAllByUser_UserIdx(userIdx)
    }
    @Transactional
    fun deleteFavorite(userIdx: Int, favoriteIdx:Int): Favorite? {
        //조건에 맞는 즐겨찾기가 있는지 확인
        val fav = favoriteRepository.findById(favoriteIdx).orElseThrow(::FavoriteNotFoundException)
        if(fav.user.userIdx == userIdx){
            favoriteRepository.deleteById(favoriteIdx)
            return fav
        }else{
            throw BadAccessException()
        }
    }

    @Transactional
    fun createFavorite(createFavoriteReq: CreateFavoriteReq): Favorite? {
        //중복된 즐겨찾기가 있는지 확인
        val fav = favoriteRepository.findByUser_UserIdxAndItem_ItemIdx(createFavoriteReq.userIdx,createFavoriteReq.itemIdx)
        if (fav != null) {
            throw FavoriteDuplicateException()
        }
        val result = Favorite(user = userRepository.findById(createFavoriteReq.userIdx).orElseThrow(::UserNotFoundException),
            category = categoryRepository.findById(createFavoriteReq.categoryIdx).orElseThrow(::CategoryNotFoundException),
            item = itemRepository.findById(createFavoriteReq.itemIdx).orElseThrow(::ItemNotFoundException))
        return favoriteRepository.save(result)
    }
}
