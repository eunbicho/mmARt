package com.ssafy.mmart.domain.productDetailImage

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.productDetail.ProductDetail
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class ProductDetailImage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productDetailImageIdx")
    var productDetailImageIdx: Int? = null,

    @Column(name = "image")
    var image : String,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="productDetailIdx")
    var productDetail: ProductDetail,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="productIdx")
    var product: Product,

    ): Base()