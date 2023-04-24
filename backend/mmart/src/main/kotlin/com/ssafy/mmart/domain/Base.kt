package com.ssafy.mmart.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class Base {
    @CreatedDate
    @Column(updatable = false)
    var createTime: LocalDateTime? = null

    @LastModifiedDate
    var updateTime: LocalDateTime? = null
}