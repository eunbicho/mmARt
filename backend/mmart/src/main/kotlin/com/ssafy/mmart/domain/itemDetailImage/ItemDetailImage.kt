package com.ssafy.mmart.domain.itemDetailImage

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.itemDetail.ItemDetail
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class ItemDetailImage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemDetailImageIdx")
    val itemDetailImageIdx: Int? = null,

    @Column(name = "image")
    val image : String,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemDetailIdx")
    val itemDetail: ItemDetail,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    val item: Item,

    ): Base()