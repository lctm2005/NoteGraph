package com.licong.notemap.service.internal;

import com.licong.notemap.domain.Link;
import com.licong.notemap.repository.LinkRepository;
import com.licong.notemap.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public Iterable<Link> findAll() {
        return linkRepository.findAll();
    }
}
