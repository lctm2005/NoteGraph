package com.licong.notemap.repository.neo4j;

import com.licong.notemap.service.domain.Note;
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
public interface NoteRepository extends Neo4jRepository<Note, UUID> {

    @Query(value = "MATCH (n:NOTE) WHERE n.title=~$title RETURN n",
            countQuery = "MATCH (n:NOTE) WHERE n.title=~$title RETURN count(n)")
    Page<Note> findByTitleLike(String title, Pageable pageable);

    @Query(value = "MATCH (n:NOTE)-[:TAG]->(t:TAG) WHERE n.title=~$title AND t.id=$tag RETURN n",
            countQuery = "MATCH (n:NOTE)-[:TAG]->(t:TAG) WHERE n.title=~$title AND t.id=$tag RETURN count(n)")
    Page<Note> findByTitleLikeAndTagEquals(String title, String tag, Pageable pageable);

    @Query("match (n:NOTE)-[:REF]-(k:NOTE) WHERE n.id=$id return k")
    List<Note> neighbours(UUID id);

    List<Note> findByIdIn(List<UUID> ids);

    @Query(value = "MATCH (n:NOTE)-[:TAG]->(t:TAG) WHERE t.id=$tag RETURN n",
            countQuery = "MATCH (n:NOTE)-[:TAG]->(t:TAG) WHERE t.id=$tag  RETURN count(n)")
    Page<Note> findByTagEquals(String tag, Pageable pageable);
}