package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "MEANING", indexes = {
        @Index(name = "IDX_MEANING_WORD_ID", columnList = "WORD_ID")
})
@Entity
@Data
@Accessors(chain = true)
public class Meaning {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Column(name = "DEFINITION_")
    private String definition;

    @JoinColumn(name = "WORD_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Word word;
}
