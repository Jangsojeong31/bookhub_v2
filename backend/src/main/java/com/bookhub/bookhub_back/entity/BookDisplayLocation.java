package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.DisplayType;
import com.bookhub.bookhub_back.entity.datetime.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_display_locations")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookDisplayLocation extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn",  nullable = false)
    private Book bookIsbn;

    @Column(name = "floor", nullable = false)
    private String floor;

    @Column(name = "hall", nullable = false)
    private String hall;

    @Column(name = "section", nullable = false)
    private String section;

    @Enumerated(EnumType.STRING)
    @Column(name = "display_type", nullable = false)
    private DisplayType displayType;

    @Lob
    @Column(name = "display_note")
    private String note;
}
