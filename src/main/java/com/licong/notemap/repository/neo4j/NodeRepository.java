package com.licong.notemap.repository.neo4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "note", path = "note")
public interface NodeRepository extends Neo4jRepository<Node, Long> {

    Optional<Node> findByUniqueId(UUID uuid);

    List<Node> findByUniqueIdIn(List<String> uuids);

    Page<Node> findByTitleContains(String title, Pageable pageable);

}