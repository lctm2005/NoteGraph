package com.licong.wiki.repository;

import com.licong.wiki.domain.Link;
import com.licong.wiki.domain.Note;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author licong
 * @date 15-8-17
 */
@Repository
public interface LinkRepository extends PagingAndSortingRepository<Link, Long> {
}
