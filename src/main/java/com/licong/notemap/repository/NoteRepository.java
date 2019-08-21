package com.licong.notemap.repository;

import com.licong.notemap.domain.Note;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "knowledge", path = "knowledge")
public interface NoteRepository extends Neo4jRepository<Note, Long> {

}