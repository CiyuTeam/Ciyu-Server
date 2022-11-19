package com.ciyu.app.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.annotations.CascadeType;

@Table(name = "BOND", indexes = {
        @Index(name = "IDX_BOND_WORD_ID", columnList = "WORD_ID")
})
@Entity @Data
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE BOND SET DELETED = true WHERE id=?")
@Where(clause = "DELETED = false")
public class Bond {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @JoinColumn(name = "WORD_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Word word;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "bond")
    @JsonManagedReference
    private List<Meet> meets;

    @Column(name = "DELETED")
    private boolean deleted = false;
}
