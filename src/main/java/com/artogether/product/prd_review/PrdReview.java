package com.artogether.product.prd_review;


import com.artogether.common.member.Member;
import com.artogether.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "prd_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrdReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prd_id", referencedColumnName = "id", nullable = false) // 外來鍵
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false) // 外來鍵
    private Member member;

    @Column(name = "review", length = 1000)
    private String content;

    @Column(name = "star", nullable = false)
    private Byte stars;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrdReview prdReview = (PrdReview) o;
        return Objects.equals(id, prdReview.id);
    }

    
}
