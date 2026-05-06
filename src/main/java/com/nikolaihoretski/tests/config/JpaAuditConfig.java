package com.nikolaihoretski.tests.config;

import com.nikolaihoretski.tests.audit.EntityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "entityAuditAware")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<UUID> entityAuditAware() {
        return new EntityAuditorAware();
    }

}
