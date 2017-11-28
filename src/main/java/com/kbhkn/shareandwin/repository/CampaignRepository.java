package com.kbhkn.shareandwin.repository;

import com.kbhkn.shareandwin.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * Created by kbhkn on 11/25/17.
 */

/*
* The RepositoryRestResource is quite useful for JPA and Rest API(via Hateoas).
* Because this Annotation automatically generates all JPA Transactions and Rest URL's.
* For more information about this API;
* https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.resource-discoverability
* */
@RepositoryRestResource(collectionResourceRel = "campaign", path = "campaign")
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Optional<List<Campaign>> findByCampaignSubject(String campaignSubject);
}
