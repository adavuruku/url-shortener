package com.example.url_shortener.Config.openapidoc;

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
        summary = "Redirect to long URL",
        description = "Redirects (HTTP 302) to the original long URL"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "302",
                description = "Redirects (HTTP 302) to the original long URL"
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
                                       "title": "We couldn't find a shortened URL for the code: rkRaF91.",
                                       "status": 404,
                                       "detail": "We couldn't find a shortened URL for the code: rkRaF91.",
                                       "instance": "/r/rkRaF91"
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
        )
})
public @interface UrlRedirectApiDoc {
}
