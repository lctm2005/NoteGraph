package com.licong.notemap.web.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GraphVo {
    private List<NodeVo> nodes = new ArrayList<>();
    private List<LinkVo> links = new ArrayList<>();
}
