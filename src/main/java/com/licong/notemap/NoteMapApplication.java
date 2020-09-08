package com.licong.notemap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lctm2005 on 2015/6/20.
 */
//@EnableRedisHttpSession
@EntityScan(basePackages = "com.licong.notemap.repository")
@EnableMongoRepositories("com.licong.notemap.repository.mongo")
@EnableNeo4jRepositories("com.licong.notemap.repository.neo4j")
@EnableTransactionManagement
@SpringBootApplication
public class NoteMapApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NoteMapApplication.class, args);
    }
}
