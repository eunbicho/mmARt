package com.ssafy.mmart.service

import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserReq
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
    fun getUser(userIdx: Int): User? {
        return userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
    }

    fun createUser(userReq: UserReq): User? {
        val oldUser = userRepository.findByEmail(userReq.email)
        return if (oldUser == null) {
            userRepository.save(userReq.toEntity(amazonS3Service.getQRCodeImage(userReq.email)!!))
        } else {
            throw EmailDuplicateException()
        }
    }

    @Transactional
    fun deleteUser(userIdx: Int): User? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        userRepository.deleteById(userIdx)
        return user
    }
}
