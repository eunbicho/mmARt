package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.favoriteCategory.Favorite
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteRepository : JpaRepository<Favorite, Int>{
    fun findAllByUser_UserIdx(userIdx: Int): List<Favorite>?
    fun findByUser_UserIdxAndItem_ItemIdx(userIdx: Int,itemIdx:Int): Favorite?
}

//@Repositorys
//class UserRepositorySupport(
//    @Resource(name = "jpaQueryFactory")
//    val query: JPAQueryFactory
//) : QuerydslRepositorySupport(User::class.java) {
//    fun findById(userIdx: Int): User? {
//        return query.selectFrom(QUser.user)
//            .where(QUser.user.userIdx.eq(userIdx))
//            .fetchOne()
//    }
//}