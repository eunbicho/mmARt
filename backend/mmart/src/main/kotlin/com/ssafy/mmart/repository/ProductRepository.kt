package com.ssafy.mmart.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.user.QUser
import com.ssafy.mmart.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.annotation.Resource

@Repository
interface ProductRepository : JpaRepository<Product, Int>

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