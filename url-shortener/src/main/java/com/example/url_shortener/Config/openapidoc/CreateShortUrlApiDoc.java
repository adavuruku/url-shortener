package com.example.url_shortener.Config.openapidoc;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 11:51 PM
 **/
import com.example.url_shortener.Models.Response.UrlCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Create a short URL",
        description = "Creates a short URL for a given long URL"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Short Url Created",
                content = @Content(
                        schema = @Schema(implementation = UrlCreateResponse.class),
                        examples = @ExampleObject(
                                value = """
                                    {
                                         "code": "S93PDI",
                                         "shortUrl": "http://localhost:9090/r/S93PDI"
                                     }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetailSchema.class),
                        examples = @ExampleObject(
                                value = """
                                    {
                                         "type": "https://api.example.com/problems/validation.error",
                                         "title": "One or more validation errors occurred.",
                                         "status": 400,
                                         "instance": "/api/urls",
                                         "errors": {
                                             "longUrl": [
                                                 "must be a valid URL"
                                             ]
                                         }
                                     }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "429",
                description = "Too many requests",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetailSchema.class)
                )
        )
})
public @interface CreateShortUrlApiDoc {
}

