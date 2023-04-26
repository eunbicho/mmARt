package com.ssafy.mmart.domain.itemItemCoupon

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
class ItemItemCoupon (
    @Id
    @Column(name = "itemItemCouponIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var itemItemCouponIdx : Int? = null,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    var itemIdx: Item,


    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemCouponIdx")
    var itemCoupon: ItemCoupon,
): Base()