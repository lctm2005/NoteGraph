package com.licong.notemap.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author licong
 * @date 15-8-17
 */
@Data
@Entity
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    @Type(type = "uuid-char")
    @Column
    private UUID uuid;

    @Column
    private String name;

    @Column
    private Date createTime = new Date();
}
