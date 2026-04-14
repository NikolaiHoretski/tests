package com.nikolaihoretski.tests.config;

import com.nikolaihoretski.tests.audit.EntityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "entityAuditAware")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<Long> entityAuditAware() {
        return new EntityAuditorAware();
    }

}
