package com.ssafy.mmart.domain.review

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.user.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class Review (
    @Id
    @Column(name = "reviewIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reviewIdx : Int? = null,

    @Column(name = "content")
    var content : String? = "",

    @Column(name = "star")
    var star : Int,

    @Column(name = "isPositive")
    var isPositive: Boolean = true,


    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    val item : Item,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="paymentDetailIdx")
    val paymentDetail: PaymentDetail,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="userIdx")
    val user : User,
): Base()