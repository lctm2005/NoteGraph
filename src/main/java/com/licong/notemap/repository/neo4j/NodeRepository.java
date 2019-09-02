package com.licong.notemap.repository.neo4j;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "note", path = "note")
public interface NodeRepository extends Neo4jRepository<Node, Long> {

    Optional<Node> findByUniqueId(UUID uuid);

    //    @Query("MATCH (n:Note) WHERE n.uuid IN {uuids} RETURN n")
    List<Node> findByUniqueIdIn(List<String> uuids);
}