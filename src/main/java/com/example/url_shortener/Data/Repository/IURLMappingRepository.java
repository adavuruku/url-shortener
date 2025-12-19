package com.example.url_shortener.Data.Repository;

import com.example.url_shortener.Data.Entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:41 PM
 **/
@Repository
@EnableJpaRepositories
public interface IURLMappingRepository extends JpaRepository<UrlMapping,Long> {
    @Modifying
    @Query("""
      update UrlMapping u
      set u.hitCount = u.hitCount + 1
      where u.code = :code
    """)
    void incrementHitCount(@Param("code") String code);

    Optional<UrlMapping> findByCode(String code);
    Optional<UrlMapping> findByLongUrl(String longUrl);
    void deleteByCode(String code);
}
