package com.example.url_shortener.Services.Implementation;

import com.example.url_shortener.Config.ShortUrlProperties;
import com.example.url_shortener.Data.Entity.UrlMapping;
import com.example.url_shortener.Data.Repository.IURLMappingRepository;
import com.example.url_shortener.Exceptions.CodeExpiredException;
import com.example.url_shortener.Exceptions.FailledToCompleteOperationException;
import com.example.url_shortener.Exceptions.NotFoundException;
import com.example.url_shortener.Exceptions.RemoteServerException;
import com.example.url_shortener.Services.Interface.IUrlMappingService;
import com.example.url_shortener.Utils.Base62CodeGenerator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 2:41 PM
 **/
public class UrlMappingService implements IUrlMappingService {

    private final IURLMappingRepository repository;
    private final Base62CodeGenerator generator;
    private final ShortUrlProperties properties;
    private final Counter redirectCounter;
    private final ApplicationContext applicationContext;
    public UrlMappingService(
            IURLMappingRepository repository,
            Base62CodeGenerator generator,
            ShortUrlProperties properties,
            MeterRegistry registry,
            ApplicationContext applicationContext
    ) {
        this.repository = repository;
        this.generator = generator;
        this.properties = properties;
        this.applicationContext = applicationContext;
        this.redirectCounter =
                registry.counter("shortener_redirect_total");
    }

    @Override
    @Modifying
    @Transactional
    @Async("taskExecutor")
    public void incrementHitCount(String code) {
        repository.incrementHitCount(code);
        redirectCounter.increment();
    }


    @Override
    public String resolveLongUrl(String code) {
        UrlMapping mapping = getURlMetadataByCode(code);
        return mapping.getLongUrl();
    }


    @Override
    public UrlMapping create(String longUrl) {
        return repository.findByLongUrl(longUrl)
                .orElseGet(() -> createNew(longUrl));
    }


    @Override
    public UrlMapping generateUrlMetaData(String code){
        return getURlMetadataByCode(code);
    }


    @Cacheable(
            cacheNames = "urlByCode",
            key = "#code"
    )
    private UrlMapping getURlMetadataByCode(String code){
        UrlMapping mapping = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(new Object[]{code}));
        if (mapping.getExpiresAt() != null &&
                mapping.getExpiresAt().isBefore(Instant.now())) {

            //call delete in background to remove code from db and cache
            UrlMappingService urlMappingService = applicationContext.getBean(UrlMappingService.class);
            urlMappingService.delete(code);

            throw new CodeExpiredException(new Object[]{code});
        }
        return mapping;
    }

    @Modifying
    @Transactional
    public UrlMapping createNew(String longUrl) {

        for (int attempt = 1; attempt <= properties.getMaxCreateRetry(); attempt++) {
            try {
                UrlMapping mapping = new UrlMapping();
                mapping.setCode(generator.generate());
                mapping.setLongUrl(longUrl);
                mapping.setCreatedAt(Instant.now());

                long expiryMinutes = properties.getExpiryMinutes();
                if (expiryMinutes > 0) {
                    mapping.setExpiresAt(
                            mapping.getCreatedAt().plus(expiryMinutes, ChronoUnit.MINUTES)
                    );
                }

                return repository.save(mapping);

            } catch (DataIntegrityViolationException ex) {
                if (attempt == properties.getMaxCreateRetry()) {
                    throw new FailledToCompleteOperationException(new Object[]{properties.getMaxCreateRetry()});
                }
            }catch (Exception e){
                //log message
                throw new RemoteServerException(new Object[]{});
            }
        }
        throw new RemoteServerException(new Object[]{});
    }


    @CacheEvict(cacheNames = "urlByCode", key = "#code")
    @Async("taskExecutor")
    public void delete(String code) {
        repository.deleteByCode(code);
    }
}
