CREATE TABLE url_mappings (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(12) NOT NULL,
    long_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE,
    hit_count BIGINT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX ux_url_mapping_code
    ON url_mappings(code);

CREATE UNIQUE INDEX ux_url_mapping_long_url
    ON url_mappings(long_url);
