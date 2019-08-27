package com.licong.notemap.repository.neo4j;

import com.licong.notemap.domain.Link;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "link", path = "link")
public interface LinkRepository extends Neo4jRepository<Link, Long> {

}