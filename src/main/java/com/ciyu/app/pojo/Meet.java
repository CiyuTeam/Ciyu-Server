package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "MEET", indexes = {
        @Index(name = "IDX_MEET_BOND_ID", columnList = "BOND_ID")
})
@Entity @Data @Accessors(chain = true)
public class Meet {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Column(name = "ARTICLE_ID")
    private String articleId;

    @Column(name = "QUALITY")
    private Double quality;

    @CreationTimestamp
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @JoinColumn(name = "MEANING_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Meaning meaning;

    @JoinColumn(name = "BOND_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Bond bond;
}
