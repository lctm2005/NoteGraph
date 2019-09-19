package com.licong.notemap.web.vo.graph;

import com.licong.notemap.web.vo.note.LinkResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GraphVo  {
    private List<NodeVo> nodes = new ArrayList<>();
    private List<LinkResource> links = new ArrayList<>();
}
