package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.CreateUserReq
import com.ssafy.mmart.repository.UserRepository
import com.ssafy.mmart.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController @Autowired constructor(
    val userService: UserService,
    private val userRepository: UserRepository
) {
    @GetMapping
    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Int): ResponseEntity<Any> {
        val user = userService.getUser(userId)
        return ResponseEntity.ok().body(user)
    }

    @PostMapping
    fun saveUser(createUserReq: CreateUserReq): ResponseEntity<Any> {
        val newUser = userService.saveUser(createUserReq)
        return ResponseEntity.ok().body(newUser)
    }
}