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
    var itemDetailImageIdx: Int? = null,

    @Column(name = "image")
    var image : String,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemDetailIdx")
    var itemDetail: ItemDetail,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    var item: Item,

    ): Base()