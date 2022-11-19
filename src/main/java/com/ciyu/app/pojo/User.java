package com.ciyu.app.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Table(name = "USER") @Accessors(chain = true)
@Entity
@Data
public class User {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(generator="system_uuid")
    @GenericGenerator(name="system_uuid",strategy="uuid")
    private String id;

    @Pattern(message = "不是正确的手机号格式", regexp = "^[1]\\d{10}$")
    @Column(name = "PHONE", nullable = false)
    @NotNull
    private String phone;

    @Column(name = "PASSWORD")
    protected String password;

    @Column(name = "NICKNAME")
    private String nickname;

    @JoinColumn(name = "GLOSSARY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Glossary glossary;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;
}
