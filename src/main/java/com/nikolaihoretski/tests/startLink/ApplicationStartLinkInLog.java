package com.nikolaihoretski.tests.startLink;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartLinkInLog {

    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationUrl(@NonNull ApplicationReadyEvent event) {
        final Environment environment = event.getApplicationContext().getEnvironment();
        final String port = environment.getProperty("local.server.port");
        final String contextPath = environment.getProperty("server.servlet.context-path", "");

        log.info("\nSwagger UI: http://localhost:{}{}/swagger-ui/index.html", port, contextPath);
    }

}
