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
    val itemIdx : Int? = null,

    @Column(name = "itemName")
    val itemName : String,

    @Column(name = "price")
    val price : Int,

    @Column(name = "inventory")
    var inventory : Int,

    @Column(name = "barcode")
    val barcode : String,

    @Column(name = "thumbnail")
    val thumbnail : String? = "",

    @Column(name = "placeInfo")
    val placeInfo : String,

    @Column(name = "weight")
    val weight : Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="categoryIdx")
    val category : Category,
): Base()