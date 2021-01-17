package com.licong.notemap.repository.neo4j;

import com.licong.notemap.service.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 */
@Repository
public interface TagRepository extends Neo4jRepository<Tag, UUID> {
    @Query(value = "MATCH (n:TAG) WHERE n.title=~$title RETURN n", countQuery = "MATCH (n:TAG) WHERE n.title=~$title RETURN count(n)")
    Page<Tag> findByTitleLike(String title, Pageable pageable);

    List<Tag> findByTitleIn(List<String> title);
}