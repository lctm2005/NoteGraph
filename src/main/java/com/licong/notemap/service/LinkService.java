package com.licong.notemap.service;

import com.licong.notemap.repository.neo4j.Link;

import java.util.List;

public interface LinkService {

    List<Link> findAll();
}
