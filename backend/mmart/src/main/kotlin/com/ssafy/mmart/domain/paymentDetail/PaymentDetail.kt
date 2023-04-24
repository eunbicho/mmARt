package com.ssafy.mmart.domain.paymentDetail

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.payment.Payment
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class PaymentDetail (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentDetailIdx")
    var paymentDetailIdx: Int? = null,

    @Column(name = "amount")
    var amount: Int,

    @Column(name = "discount")
    var discount: Int,

    @Column(name = "costResult")
    var costResult: Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="paymentIdx")
    var payment: Payment,

//    @ManyToOne
//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @JoinColumn(name="productIdx")
//    var product: Product,
): Base()