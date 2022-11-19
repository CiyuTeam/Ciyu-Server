package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Table(name = "GLOSSARY")
@Entity
public class Glossary {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Column(name = "NAME")
    private String name;

    @JoinTable(name = "GLOSSARY_WORD_LINK",
            joinColumns = @JoinColumn(name = "GLOSSARY_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Word> words;
}
