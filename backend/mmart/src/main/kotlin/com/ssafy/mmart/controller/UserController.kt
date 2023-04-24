package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.CreateUserReq
import com.ssafy.mmart.repository.UserRepository
import com.ssafy.mmart.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController @Autowired constructor(
    val userService: UserService,
    private val userRepository: UserRepository
) {
    @GetMapping
    fun getUsers(): ResultResponse<List<User>> {
        return ResultResponse.success(userRepository.findAll())
    }

    @GetMapping("/{userIdx}")
    fun getUser(@PathVariable userIdx: Int): ResultResponse<User?> {
        return ResultResponse.success(userService.getUser(userIdx))
    }

    @PostMapping
    fun saveUser(@RequestBody createUserReq: CreateUserReq): ResultResponse<User?> {
//        val newUser = userService.saveUser(createUserReq)
        return ResultResponse.success(userService.saveUser(createUserReq))
//        return ResponseEntity.ok().body(newUser)
    }
}