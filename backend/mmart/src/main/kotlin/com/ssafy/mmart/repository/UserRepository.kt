package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}

//@Repository
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