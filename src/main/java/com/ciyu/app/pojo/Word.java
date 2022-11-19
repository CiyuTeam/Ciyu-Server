package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data @Accessors(chain = true)
@Table(name = "WORD")
@Entity
public class Word {
    @Column(name = "ID", nullable = false)
    @Id
    private String id;

    @OneToMany(mappedBy = "word")
    @JsonManagedReference
    private List<Meaning> meanings;

    @JsonManagedReference
    @OneToMany(mappedBy = "word")
    private List<Phonetic> phonetics;

    @OneToMany(mappedBy = "word")
    @JsonManagedReference
    private List<Sentence> sentences;
}
