package com.ssafy.mmart.domain.paymentDetail

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.payment.Payment
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class PaymentDetail (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentDetailIdx")
    val paymentDetailIdx: Int? = null,

    @Column(name = "amount")
    val quantity: Int,

    @Column(name = "discount")
    val discount: Int = 0,

    @Column(name = "costResult")
    val totalPrice: Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="paymentIdx")
    val payment: Payment,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    val item: Item,
): Base()