package com.licong.notemap.repository.neo4j.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ywd
 * @data 2019-08-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    private String startId;

    private String endId;
}
