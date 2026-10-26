package com.enceladus.delivery.demo.backend.controller;

import com.enceladus.delivery.demo.backend.exceptions.UnknownAccessKeyException;
import com.enceladus.delivery.demo.commons.model.Version;
import com.enceladus.delivery.demo.commons.model.core.Constants;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/")
@Tag(name = "Version Controller", description = "Display API Version Information along with artifact version")
@Slf4j
public class VersionController {
    @Value("${_DASHBOARD_API_VERSION:v1}")
    String apiPathVersion;

    @Autowired
    BuildProperties buildProperties;

    VersionController(BuildProperties buildProperties){
        this.buildProperties = buildProperties;
    }

    @Operation(summary = "Returns version information configured in property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Backend Version Information",
                    content = {@Content(mediaType = "application/json")})})
    @GetMapping(path = "/version", produces = "application/json")
    public Mono<DashboardResponse<Version>> getVersion(@RequestHeader(name = Constants.API_KEY_HEADER_NAME, required = true) @NonNull String token)
            throws UnknownAccessKeyException {
        if (!Constants.DASHBOARD_API_KEY.equals(token)) {
            throw new UnknownAccessKeyException("Invalid header value");
        }
        return Mono.just(Version.with("/api/" + apiPathVersion, buildProperties))
                .map(version -> DashboardResponse.<Version>builder().
                        data(version).status("SUCCESS").message("Version retrieved successfully.").build());
    }
}
