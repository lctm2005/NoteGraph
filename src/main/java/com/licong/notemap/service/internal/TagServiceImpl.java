package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.TagRepository;
import com.licong.notemap.service.TagService;
import com.licong.notemap.service.domain.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Page<Tag> findByTitleContains(String title, Pageable pageable) {
        return tagRepository.findByTitleLike(title, pageable);
    }
}
