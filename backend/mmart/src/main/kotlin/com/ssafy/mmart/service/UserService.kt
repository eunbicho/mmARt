package com.ssafy.mmart.service;

import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.CreateUserReq
import com.ssafy.mmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserService @Autowired constructor(
    val userRepository : UserRepository,
){
    fun getUser(userId: Int): User? {
        return userRepository.findById(userId).get()
    }

    fun saveUser(createUserReq: CreateUserReq): User? {
        return userRepository.save(createUserReq.toEntity())
    }
}
