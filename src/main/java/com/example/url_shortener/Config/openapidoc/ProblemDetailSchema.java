package com.example.url_shortener.Config.openapidoc;

import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 11:53 PM
 **/

@Schema(
        name = "ProblemDetail",
        description = "RFC 7807 Problem Details"
)
public class ProblemDetailSchema {

    public URI type;
    public String title;
    public int status;
    public String detail;
    public URI instance;
    public Map<String, List<String>> errors;
}
