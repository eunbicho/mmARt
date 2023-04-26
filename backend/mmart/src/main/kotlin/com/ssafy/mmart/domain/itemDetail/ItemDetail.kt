package com.ssafy.mmart.domain.itemDetail

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class ItemDetail (
    @Id
    @Column(name = "itemDetailIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var itemDetailIdx : Int? = null,

    @Column(name = "content")
    var content : String? = "",

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    var item : Item,
): Base()