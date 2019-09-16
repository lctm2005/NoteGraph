package com.licong.notemap.web.controller;

import com.licong.notemap.repository.neo4j.Node;
import com.licong.notemap.repository.neo4j.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrapController {

    @Autowired
    private NodeRepository nodeRepository;


    @RequestMapping(value = "/api/graph", method = RequestMethod.GET)
    public ResponseEntity<PagedResources<Node>> search(@RequestParam("title") String title,
                                                       Pageable pageable) {
        Page<Node> page = nodeRepository.findByTitleContains(title, pageable);
        return null;
    }
}
