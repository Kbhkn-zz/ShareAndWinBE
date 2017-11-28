package com.kbhkn.shareandwin.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by kbhkn on 11/25/17.
 */
@Entity
public class Token implements Serializable {
    //H2 Embedded DB, sometimes can't generated an ID.
    //For this reason, we have the use GenericGenerator.
    @Id
    @GenericGenerator(name = "id_generator_user", strategy = "increment")
    @GeneratedValue(generator = "id_generator_user")
    @Column(name = "id", unique = true, nullable = false)
    private long tokenId;

    @Column
    private String token;

    @Column
    private long count;

    public Token() {
    }

    public Token(String token, long count) {
        this.token = token;
        this.count = count;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("tokenId=").append(tokenId);
        sb.append(", token='").append(token).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
