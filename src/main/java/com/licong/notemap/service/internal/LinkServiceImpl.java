package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.Link;
import com.licong.notemap.repository.neo4j.LinkRepository;
import com.licong.notemap.service.LinkService;
import com.licong.notemap.service.domain.NoteLink;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<NoteLink> findAll() {
        List<Link> links = new ArrayList<>();
        CollectionUtils.addAll(links, linkRepository.findAll().iterator());
        return links.stream().map(e -> NoteLink.convert(e)).collect(Collectors.toList());
    }
}
