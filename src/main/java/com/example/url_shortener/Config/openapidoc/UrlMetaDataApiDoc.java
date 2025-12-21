package com.example.url_shortener.Config.openapidoc;

import com.example.url_shortener.Models.Response.UrlMetaDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 12:02 AM
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get URL metadata",
        description = "Returns metadata for a short URL"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Metadata found",
                content = @Content(
                        schema = @Schema(implementation = UrlMetaDataResponse.class),
                        examples = @ExampleObject(
                                value = """
                                   {
                                            "code": "rkRaF9",
                                            "creationTime": "2025-12-19T22:33:30.671819Z",
                                            "invocationCounter": 1
                                        }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Short URL not found",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetailSchema.class),
                        examples = @ExampleObject(
                                value = """
                                   {
                                           "type": "https://api.example.com/problems/code.not.found",
                                           "title": "We couldn't find a shortened URL for the code: S93PDI.",
                                           "status": 404,
                                           "detail": "We couldn't find a shortened URL for the code: S93PDI.",
                                           "instance": "/api/urls/S93PDI"
                                       }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "410",
                description = "Expired shortened code",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetailSchema.class),
                        examples = @ExampleObject(
                                value = """
                                   {
                                          "type": "https://api.example.com/problems/code.expired",
                                          "title": "The shortened URL for this code has expired: S93PDI.",
                                          "status": 410,
                                          "detail": "The shortened URL for this code has expired: S93PDI.",
                                          "instance": "/api/urls/S93PDI"
                                      }
                                """
                        )
                )
        ),
})
public @interface UrlMetaDataApiDoc {
}
