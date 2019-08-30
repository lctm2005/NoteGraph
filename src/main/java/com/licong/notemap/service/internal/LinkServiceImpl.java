package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.Link;
import com.licong.notemap.repository.neo4j.LinkRepository;
import com.licong.notemap.service.LinkService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> findAll() {
        List<Link> links = new ArrayList<>();
        CollectionUtils.addAll(links, linkRepository.findAll().iterator());
        return links;
    }
}
