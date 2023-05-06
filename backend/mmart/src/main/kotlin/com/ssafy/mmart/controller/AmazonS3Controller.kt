package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.service.AmazonS3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1/image")
class AmazonS3Controller @Autowired constructor(val amazonS3Service: AmazonS3Service) {

    @PostMapping
    fun uploadImage(@RequestPart multipartFile: MultipartFile?): ResultResponse<String?> {
        return ResultResponse.success(amazonS3Service.uploadImage(multipartFile!!))
    }

    @DeleteMapping
    fun deleteImage(@RequestParam fileName: String?): ResultResponse<Void?> {
        amazonS3Service.deleteImage(fileName)
        return ResultResponse.success(null)
    }

    @PostMapping("/item")
    fun uploadItemImage(@RequestParam url:String,itemIdx:Int): ResultResponse<String?> {
        return ResultResponse.success(amazonS3Service.uploadItemImage(url,itemIdx))
    }
}