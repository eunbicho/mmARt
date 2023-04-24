package com.ssafy.mmart.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Column

class Base {
    @CreatedDate
    @Column(updatable = false)
    private val createTime: LocalDateTime? = null

    @LastModifiedDate
    private val updateTime: LocalDateTime? = null
}