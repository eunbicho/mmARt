package com.ssafy.mmart.domain.productDetail

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.product.Product
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class ProductDetail (
    @Id
    @Column(name = "productDetailIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productDetailIdx : Int? = null,

    @Column(name = "content")
    var content : String? = "",

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="productIdx")
    var product : Product,
): Base()