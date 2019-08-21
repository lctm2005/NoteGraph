package com.licong.notemap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lctm2005 on 2015/6/20.
 */
@EntityScan(basePackages = "com.licong.notemap.domain")
@EnableNeo4jRepositories("com.licong.notemap.repository")
@EnableTransactionManagement
@SpringBootApplication
public class NoteMapApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NoteMapApplication.class, args);
    }
}
