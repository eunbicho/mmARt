package com.ssafy.mmart.domain.user

import com.ssafy.mmart.domain.Base
import org.hibernate.annotations.ColumnDefault
import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx")
    var userIdx: Int? = null,

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "point")
    @ColumnDefault("0")
    var point: Int? = 0,
):Base()