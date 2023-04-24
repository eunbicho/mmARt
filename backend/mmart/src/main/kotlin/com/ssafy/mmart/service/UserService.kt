package com.ssafy.mmart.service;

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.user.QUser.user
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.CreateUserReq
import com.ssafy.mmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserService
//@Autowired constructor
    (
//    val userRepository : UserRepository,
){
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jpaQueryFactory: JPAQueryFactory
    fun getUser(userId: Int): User? {
//        return userRepository.findById(userId).get()
        return jpaQueryFactory
            .selectFrom(user)
            .where(user.userIdx.eq(userId))
            .fetchOne()
    }

    fun saveUser(createUserReq: CreateUserReq): User? {
        return userRepository.save(createUserReq.toEntity())
    }
}
