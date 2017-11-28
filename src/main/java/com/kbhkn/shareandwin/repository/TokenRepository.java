package com.kbhkn.shareandwin.repository;

import com.kbhkn.shareandwin.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * Created by kbhkn on 11/25/17.
 */

/*
* The RepositoryRestResource most usable for JPA and RestAPI(With Hateoas)
* Because this Annotation auto generate all JPA Operations and Rest URL's.
* For more information about this API;
* https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.resource-discoverability
* */
@RepositoryRestResource(collectionResourceRel = "token", path = "token")
public interface TokenRepository extends JpaRepository<Token, Long> {
    //Find The User's used token.
    Optional<Token> findTokenByToken(String token);

//    @RestResource(path = "checkToken")
//    @Query("SELECT tokenCount FROM Token t WHERE t.token = :token")
//    Long read (@Param("token") String token);
}