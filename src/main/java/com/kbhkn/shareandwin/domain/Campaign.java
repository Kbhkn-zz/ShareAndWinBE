package com.kbhkn.shareandwin.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kbhkn on 11/25/17.
 */
@Entity
@Table(name = "CAMPAIGN")
public class Campaign implements Serializable {
    //H2 Embedded DB, sometimes can't generated an ID.
    //For this reason, we have the use GenericGenerator.
    @Id
    @GenericGenerator(name = "id_generator_campaign", strategy = "increment")
    @GeneratedValue(generator = "id_generator_campaign")
    @Column(name = "id", unique = true, nullable = false)
    private long campaignId;

    @Column
    private String campaignSubject;

    public Campaign() {
    }

    public Campaign(String campaignSubject) {
        this.campaignSubject = campaignSubject;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignSubject() {
        return campaignSubject;
    }

    public void setCampaignSubject(String campaignSubject) {
        this.campaignSubject = campaignSubject;
    }
}
