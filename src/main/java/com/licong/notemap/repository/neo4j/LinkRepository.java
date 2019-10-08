package com.licong.notemap.repository.neo4j;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "link", path = "link")
public interface LinkRepository extends Neo4jRepository<Link, Long> {

    void mergeAll(List<Link> links);

    List<Link> findByStartInOrEndIn(List<String> nodeIds);

    List<Link> findByStartAndEndAndTitle(List<Link> links);

}