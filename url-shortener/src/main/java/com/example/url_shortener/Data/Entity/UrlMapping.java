package com.example.url_shortener.Data.Entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:21 PM
 **/
@Entity
@Table(
        name = "url_mappings",
        indexes = {
                @Index(name = "idx_code", columnList = "code", unique = true),
                @Index(name = "idx_long_url", columnList = "longUrl", unique = true)
        }
)
public class UrlMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12, unique = true)
    private String code;

    @Column(name = "long_url",nullable = false, length = 2048, unique = true)
    private String longUrl;

    @Column(name = "created_at",  nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "hit_count", nullable = false)
    private long hitCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }
}

