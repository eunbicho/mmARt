package com.ssafy.mmart.service;

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.user.QUser.user
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserReq
import com.ssafy.mmart.exception.conflict.EmailDuplicateException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.findByIdOrNull
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
        return userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
    }

    fun createUser(userReq: UserReq): User? {
        var otherUser = userRepository.findByEmail(userReq.email)
        return if (otherUser == null) {
            userRepository.save(userReq.toEntity())
        } else {
            throw EmailDuplicateException()
        }
    }

    @Transactional
    fun updateUser(userIdx: Int, userReq: UserReq): User? {
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        var otherUser = userRepository.findByEmail(userReq.email)
        if (otherUser == null) {
            user.email = userReq.email
        } else if (otherUser.userIdx != user.userIdx) {
            throw EmailDuplicateException()
        }
        user.password = userReq.password
        user.name = userReq.name
        userRepository.save(user)
        return user
    }

    @Transactional
    fun deleteUser(userIdx: Int): User? {
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        userRepository.deleteById(userIdx)
        return user
    }
}
