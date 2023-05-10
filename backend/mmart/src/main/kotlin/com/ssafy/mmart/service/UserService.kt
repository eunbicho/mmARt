package com.ssafy.mmart.service

import com.ssafy.mmart.domain.user.dto.LoginUserReq
import com.ssafy.mmart.domain.user.dto.UserReq
import com.ssafy.mmart.domain.user.dto.UserRes
import com.ssafy.mmart.exception.conflict.EmailDuplicateException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
    val amazonS3Service: AmazonS3Service,
    val userRepository: UserRepository,
) {
    fun getUser(userIdx: Int): UserRes? {
        val result = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return UserRes(result.userIdx!!, result.email, result.password, result.name)
    }

    fun getUserByEmail(email: String): UserRes? {
        val result = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        return UserRes(result.userIdx!!, result.email, result.password, result.name)
    }

    fun createUser(userReq: UserReq): UserRes? {
        val oldUser = userRepository.findByEmail(userReq.email)
        if (oldUser == null) {
            val result = userRepository.save(userReq.toEntity(amazonS3Service.getQRCodeImage(userReq.email)!!))
            return UserRes(result.userIdx!!, result.email, result.password, result.name)
        } else {
            throw EmailDuplicateException()
        }
    }

    @Transactional
    fun deleteUser(userIdx: Int): UserRes? {
        val result = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        userRepository.deleteById(userIdx)
        return UserRes(result.userIdx!!, result.email, result.password, result.name)
    }

    fun logInUser(loginUserReq: LoginUserReq): UserRes? {

        val result = userRepository.findByEmailAndPassword(loginUserReq.email, loginUserReq.password)
        if (result != null)
            return UserRes(result.userIdx!!, result.email, result.password, result.name)
        else
            throw UserNotFoundException()
    }
}
