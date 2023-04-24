package com.ssafy.mmart.domain.productProductCoupon

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.productCoupon.ProductCoupon
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
class ProductProductCoupon (
    @Id
    @Column(name = "productProductCouponIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productProductCouponIdx : Int? = null,

//    @ManyToOne
//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @JoinColumn(name="productIdx")
//    var productIdx: Product,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="productCouponIdx")
    var productCoupon: ProductCoupon,
): Base()