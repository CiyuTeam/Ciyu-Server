package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Table(name = "SENTENCE", indexes = {
        @Index(name = "IDX_SENTENCE_WORD_ID", columnList = "WORD_ID")
})
@Entity
public class Sentence {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TRANSLATION")
    private String translation;

    @Column(name = "SOURCE")
    private String source;

    @JoinColumn(name = "WORD_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Word word;
}
