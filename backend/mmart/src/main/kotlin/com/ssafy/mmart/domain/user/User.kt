package com.ssafy.mmart.domain.user

import com.ssafy.mmart.domain.Base
import org.hibernate.annotations.ColumnDefault
import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx")
    val userIdx: Int? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "point")
    @ColumnDefault("0")
    val point: Int? = 0,
):Base()