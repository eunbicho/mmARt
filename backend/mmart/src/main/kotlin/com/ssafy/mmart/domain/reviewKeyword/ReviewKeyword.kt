package com.ssafy.mmart.domain.reviewKeyword

import com.ssafy.mmart.domain.Base
import javax.persistence.*

@Entity
data class ReviewKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewKeywordIdx")
    val reviewKeywordIdx: Int? = null,

    @Column(name = "isPositive")
    val isPositive: Boolean = true,

    @Column(name = "keywordContent")
    val keywordContent: String,

    ) : Base()