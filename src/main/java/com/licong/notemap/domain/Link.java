package com.licong.notemap.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

/**
 * @author licong
 * @date 15-8-17
 */
@Data
@Entity
public class Link {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    @Type(type = "uuid-char")
    private UUID source;

    @Column
    @Type(type = "uuid-char")
    private UUID target;

    @Column
    private Date createTime = new Date();
}
