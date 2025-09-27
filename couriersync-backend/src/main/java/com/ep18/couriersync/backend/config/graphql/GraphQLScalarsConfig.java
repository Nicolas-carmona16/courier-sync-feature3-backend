package com.ep18.couriersync.backend.config.graphql;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * Registro de scalars que usamos en los .graphqls:
 * - LocalDate (mapea con ExtendedScalars.Date)
 * - LocalDateTime (mapea con ExtendedScalars.DateTime)
 * - Long (mapea con ExtendedScalars.GraphQLLong)
 */
@Configuration
public class GraphQLScalarsConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.GraphQLLong);
    }
}
