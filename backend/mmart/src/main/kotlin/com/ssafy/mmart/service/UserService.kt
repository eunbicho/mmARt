package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.user.QUser.user
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserReq
import com.ssafy.mmart.exception.conflict.EmailDuplicateException
import com.ssafy.mmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository,
    val jpaQueryFactory: JPAQueryFactory,
    val em: EntityManager,
) {
    fun getUser(userIdx: Int): User? {
        return jpaQueryFactory
            .selectFrom(user)
            .where(user.userIdx.eq(userIdx))
            .fetchOne()
    }

    fun createUser(userReq: UserReq): User? {
        var users: List<User?> = jpaQueryFactory
            .selectFrom(user).where(user.email.eq(userReq.email))
            .fetch()
        return if (users.isNullOrEmpty()) {
            userRepository.save(userReq.toEntity())
        } else {
            throw EmailDuplicateException()
        }
    }


    @Transactional
    fun updateUser(userIdx: Int, userReq: UserReq): User?{
        jpaQueryFactory
            .update(user)
            .where(user.userIdx.eq(userIdx))
            .set(user.email, userReq.email)
            .set(user.password, userReq.password)
            .set(user.name, userReq.name)
            .set(user.updateTime,LocalDateTime.now())
            .execute()
        em.clear()
        em.flush()
        return jpaQueryFactory
            .selectFrom(user)
            .where(user.userIdx.eq(userIdx))
            .fetchOne()
    }

    @Transactional
    fun deleteUser(userIdx: Int): Long? {
        return jpaQueryFactory
            .delete(user)
            .where(user.userIdx.eq(userIdx))
            .execute()
    }
}
