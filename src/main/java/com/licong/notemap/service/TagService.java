package com.licong.notemap.service;

import com.licong.notemap.service.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    Page<Tag> findByTitleContains(String title, Pageable pageable);

}
