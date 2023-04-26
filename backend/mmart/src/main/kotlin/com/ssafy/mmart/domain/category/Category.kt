package com.ssafy.mmart.domain.category

import com.ssafy.mmart.domain.Base
import javax.persistence.*

@Entity
data class Category (
    @Id
    @Column(name = "categoryIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryIdx : Int? = null,

    @Column(name = "categoryName")
    val categoryName : String,

    @Column(name = "placeInfo")
    val placeInfo : String,
): Base()