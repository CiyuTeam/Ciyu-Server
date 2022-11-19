package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Table(name = "PHONETIC", indexes = {
        @Index(name = "IDX_PHONETIC_WORD_ID", columnList = "WORD_ID")
})
@Entity
public class Phonetic {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Column(name = "LOCALE")
    private String locale;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "AUDIO")
    private String audio;
    @JoinColumn(name = "WORD_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Word word;
}
