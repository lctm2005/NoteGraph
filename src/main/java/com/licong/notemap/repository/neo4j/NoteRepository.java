package com.licong.notemap.repository.neo4j;

import com.licong.notemap.service.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface NoteRepository extends Neo4jRepository<Note, Long> {

    @Query(value = "MATCH (n:NOTE) WHERE n.title=~{title} RETURN n", countQuery = "MATCH (n:NOTE) WHERE n.title=~{title} RETURN count(n)")
    Page<Note> findByTitleLike(String title, Pageable pageable);

    @Query("match (n:NOTE)-[*]-(k:NOTE) where n.id={id} return k")
    List<Note> neighbours(Long id);

    List<Note> findByIdIn(List<Long> ids);
}