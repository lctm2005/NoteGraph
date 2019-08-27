package com.licong.notemap.repository.mongo;

import com.licong.notemap.domain.Note;
import com.licong.notemap.domain.NoteContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "note_content", path = "note_content")
public interface NoteContentRepository extends MongoRepository<NoteContent, Long> {

}