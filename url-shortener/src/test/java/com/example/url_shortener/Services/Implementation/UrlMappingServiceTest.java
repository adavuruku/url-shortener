package com.example.url_shortener.Services.Implementation;

import com.example.url_shortener.Config.AppConfigProperties;
import com.example.url_shortener.Data.Entity.UrlMapping;
import com.example.url_shortener.Data.Repository.IURLMappingRepository;
import com.example.url_shortener.Exceptions.CodeExpiredException;
import com.example.url_shortener.Exceptions.FailedToCompleteOperationException;
import com.example.url_shortener.Exceptions.NotFoundException;
import com.example.url_shortener.Utils.Base62CodeGenerator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 6:56 PM
 **/
@ExtendWith(MockitoExtension.class)
class UrlMappingServiceTest {

    @Mock
    IURLMappingRepository repository;

    @Mock
    Base62CodeGenerator generator;

    @Mock
    AppConfigProperties properties;

    @Mock
    AppConfigProperties.ShortUrl shortUrl;

    @Mock
    MeterRegistry meterRegistry;

    @Mock
    Counter redirectCounter;

    @Mock
    ApplicationContext applicationContext;

    @InjectMocks
    UrlMappingService service;

    void setupUrlMapping() {
        when(properties.getShortUrl()).thenReturn(shortUrl);
    }

    @Test
    @DisplayName("IncrementHitCount increments repository hit counter and Micrometer metric")
     void incrementHitCount_shouldIncrementRepoAndMetric() {
        when(meterRegistry.counter(anyString())).thenReturn(redirectCounter);
        service = new UrlMappingService(
                repository,
                generator,
                properties,
                meterRegistry,
                applicationContext
        );
        service.incrementHitCount("abc");
        verify(repository).incrementHitCount("abc");
        verify(redirectCounter).increment();
    }

    @Test
    @DisplayName("resolveLongUrl resolve and redirect longUrl by passing a code")
    void resolveLongUrl_shouldReturnLongUrl() {
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl("https://example.com");

        when(repository.findByCode("abc"))
                .thenReturn(Optional.of(mapping));

        String result = service.resolveLongUrl("abc");

        assertEquals("https://example.com", result);
    }


    @Test
    @DisplayName("generateUrlMetaData should return url metadata")
    void generateUrlMetaData_shouldReturnMapping() {
        UrlMapping mapping = new UrlMapping();

        when(repository.findByCode("abc"))
                .thenReturn(Optional.of(mapping));

        UrlMapping result = service.generateUrlMetaData("abc");

        assertSame(mapping, result);
    }

    // generateUrlMetaData

    @Test
    @DisplayName("getURlMetadataByCode should throw longUrl not found")
    void getURlMetadataByCode_notFound_shouldThrow() {
        when(repository.findByCode("abc"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getURlMetadataByCode("abc"));
    }

    @Test
    @DisplayName("getURlMetadataByCode should delete from redis (cache) and delete from db")
    void getURlMetadataByCode_expired_shouldDeleteAndThrow() {
        UrlMapping expired = new UrlMapping();
        expired.setExpiresAt(Instant.now().minusSeconds(60));

        UrlMappingService proxy = mock(UrlMappingService.class);
        when(applicationContext.getBean(UrlMappingService.class))
                .thenReturn(proxy);

        when(repository.findByCode("abc"))
                .thenReturn(Optional.of(expired));

        assertThrows(CodeExpiredException.class,
                () -> service.getURlMetadataByCode("abc"));

        verify(proxy).delete("abc");
    }

    @Test
    @DisplayName("getURlMetadataByCode should return/redirect to url metadata successfully")
    void getURlMetadataByCode_valid_shouldReturn() {
        UrlMapping mapping = new UrlMapping();

        when(repository.findByCode("abc"))
                .thenReturn(Optional.of(mapping));

        UrlMapping result = service.getURlMetadataByCode("abc");

        assertSame(mapping, result);
    }

    @Test
    @DisplayName("create_existingLongUrl return mapping if it exist during create")
    void create_existingLongUrl_shouldReturnExisting() {
        UrlMapping existing = new UrlMapping();

        when(repository.findByLongUrl("url"))
                .thenReturn(Optional.of(existing));

        UrlMapping result = service.create("url");

        assertSame(existing, result);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("create_newLongUrl should create new url mapping")
    void create_newLongUrl_shouldCreate() {
        setupUrlMapping();
        when(repository.findByLongUrl("url"))
                .thenReturn(Optional.empty());
        when(generator.generate()).thenReturn("abc");
        when(shortUrl.getExpiryMinutes()).thenReturn(0L);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UrlMapping result = service.create("url");

        assertEquals("abc", result.getCode());
        assertEquals("url", result.getLongUrl());
    }


    @Test
    @DisplayName("createUrlMapping_withExpiry_shouldSetExpiresAt")
    void createUrlMapping_withExpiry_shouldSetExpiresAt() {
        setupUrlMapping();
        when(generator.generate()).thenReturn("abc");
        when(shortUrl.getExpiryMinutes()).thenReturn(5L);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        UrlMapping result = service.createUrlMapping("url");

        assertNotNull(result.getExpiresAt());
    }


    @Test
    @DisplayName("recover_shouldThrowFailedToCompleteOperationException should throw when it fail to create for the no of retries")
    void recover_shouldThrowFailedToCompleteOperationException() {
        setupUrlMapping();
        when(shortUrl.getMaxCreateRetry()).thenReturn(3L);

        assertThrows(FailedToCompleteOperationException.class,
                () -> service.recover(
                        new DataIntegrityViolationException(""),
                        "url"
                ));
    }


    @Test
    @DisplayName("delete_shouldDeleteByCode")
    void delete_shouldDeleteByCode() {
        service.delete("abc");

        verify(repository).deleteByCode("abc");
    }
}

