package org.foo.scheduling.domain;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for job configuration loaded from YAML
 */
@Data
@ConfigurationProperties(prefix = "app")
public class JobsProperties {
    private Map<String, JobConfig> jobs;
}
