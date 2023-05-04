package com.ssafy.mmart.domain.reviewReviewKeyword

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.reviewKeyword.ReviewKeyword
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class ReviewReviewKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewReviewKeywordIdx")
    val reviewReviewKeywordIdx: Int? = null,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="reviewKeywordIdx")
    val reviewKeyword: ReviewKeyword,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="reviewIdx")
    val review: Review,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="itemIdx")
    val item : Item,


    ) : Base()