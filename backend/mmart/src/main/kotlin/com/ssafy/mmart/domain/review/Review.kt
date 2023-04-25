package com.ssafy.mmart.domain.review

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.product.Product
import com.ssafy.mmart.domain.user.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class Review (
    @Id
    @Column(name = "reviewIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var reviewIdx : Int? = null,

    @Column(name = "reviewContent")
    var reviewContent : String? = "",

    @Column(name = "star")
    var star : Int,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="productIdx")
    var product : Product,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="userIdx")
    var user : User,
): Base()