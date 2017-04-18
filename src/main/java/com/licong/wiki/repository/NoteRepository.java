package com.licong.wiki.repository;

import com.licong.wiki.domain.Note;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author licong
 * @date 15-8-17
 */
@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {
}
