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
    var paymentIdx: Int? = null,

    @Column(name = "marketName")
    var marketName: String,

    @Column(name = "date")
    var date: LocalDateTime,

    @Column(name = "pointUse")
    var pointUse: Int?,

    @Column(name = "total")
    var total: Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="userIdx")
    var user: User,
): Base()