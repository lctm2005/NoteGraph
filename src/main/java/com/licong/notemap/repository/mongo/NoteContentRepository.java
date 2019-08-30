package com.licong.notemap.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "note_content", path = "note_content")
public interface NoteContentRepository extends MongoRepository<NoteContent, UUID> {

}