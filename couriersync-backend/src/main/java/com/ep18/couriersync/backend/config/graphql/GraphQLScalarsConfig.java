package com.ep18.couriersync.backend.config.graphql;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLScalarsConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {

        // Alias para que el schema "LocalDate" use ExtendedScalars.Date
        GraphQLScalarType localDate = GraphQLScalarType.newScalar(ExtendedScalars.Date)
                .name("LocalDate")
                .build();

        // Alias para que el schema "LocalDateTime" use ExtendedScalars.DateTime
        GraphQLScalarType localDateTime = GraphQLScalarType.newScalar(ExtendedScalars.DateTime)
                .name("LocalDateTime")
                .build();

        // El schema declara "Long" y ExtendedScalars trae GraphQLLong (nombre interno: "Long")
        GraphQLScalarType longScalar = ExtendedScalars.GraphQLLong;

        return builder -> builder
                .scalar(localDate)
                .scalar(localDateTime)
                .scalar(longScalar);
    }
}