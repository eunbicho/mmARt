package com.ssafy.mmart.domain.product

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.category.Category
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class Product (
    @Id
    @Column(name = "productIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productIdx : Int? = null,

    @Column(name = "productName")
    var productName : String,

    @Column(name = "price")
    var price : Int,

    @Column(name = "inventory")
    var inventory : Int,

    @Column(name = "barcode")
    var barcode : String,

    @Column(name = "thumbnail")
    var thumbnail : String? = "",

    @Column(name = "placeInfo")
    var placeInfo : String,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="categoryIdx")
    var category : Category,
): Base()