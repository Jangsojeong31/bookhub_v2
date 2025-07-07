package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_categories")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    @JsonIgnore
    private BookCategory parentCategoryId;

    @OneToMany(mappedBy = "parentCategoryId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BookCategory> subCategories = new ArrayList<>();

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_level", nullable = false)
    private int categoryLevel = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;

    @Column(name = "category_order")
    private int categoryOrder = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_policy_id")
    private DiscountPolicy discountPolicyId;
}