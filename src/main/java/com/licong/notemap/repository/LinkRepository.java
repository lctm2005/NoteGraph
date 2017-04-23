package com.licong.notemap.repository;

import com.licong.notemap.domain.Link;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author licong
 * @date 15-8-17
 */
@Repository
public interface LinkRepository extends PagingAndSortingRepository<Link, Long> {
}
