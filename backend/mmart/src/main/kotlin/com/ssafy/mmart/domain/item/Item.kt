package com.ssafy.mmart.domain.item

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.category.Category
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class Item (
    @Id
    @Column(name = "itemIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var itemIdx : Int? = null,

    @Column(name = "itemName")
    var itemName : String,

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