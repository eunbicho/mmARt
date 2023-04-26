package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserReq
import com.ssafy.mmart.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController @Autowired constructor(
    val userService: UserService,
) {
    @GetMapping("/{userIdx}")
    fun getUser(@PathVariable userIdx: Int): ResultResponse<User?> {
        return ResultResponse.success(userService.getUser(userIdx))
    }

    @PostMapping
    fun createUser(@RequestBody userReq: UserReq): ResultResponse<User?> {
        return ResultResponse.success(userService.createUser(userReq))
    }

    @PutMapping("/{userIdx}")
    fun updateUser(@PathVariable userIdx: Int, @RequestBody userReq: UserReq): ResultResponse<User?> {
        return ResultResponse.success(userService.updateUser(userIdx, userReq))
    }

    @DeleteMapping("/{userIdx}")
    fun deleteUser(@PathVariable userIdx: Int): ResultResponse<Long?> {
        return ResultResponse.success(userService.deleteUser(userIdx))
    }
}