package com.enceladus.delivery.demo.commons.model;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;

import java.time.Instant;

@Builder
public record Version (
        String name,
        String group,
        String artifact,
        String version,
        String apiPathVersion,
        Instant time
) {
    public static Version with(@NonNull String apiPathVersion, BuildProperties buildProperties) {
        return builder()
                .apiPathVersion(apiPathVersion)
                .name(buildProperties.getName())
                .group(buildProperties.getGroup())
                .artifact(buildProperties.getArtifact())
                .version(buildProperties.getVersion())
                .time(buildProperties.getTime())
                .build();
    }
}

