package com.ssafy.mmart.domain.payment

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.user.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentIdx")
    val paymentIdx: Int? = null,

    @Column(name = "marketName")
    val marketName: String = "역삼",

    @Column(name = "date")
    val date: LocalDateTime = LocalDateTime.now(),

    @Column(name = "total")
    val total: Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="userIdx")
    val user: User,
): Base()