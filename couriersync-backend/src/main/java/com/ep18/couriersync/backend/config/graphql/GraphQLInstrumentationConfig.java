package com.ep18.couriersync.backend.config.graphql;

import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(GraphQLLimitsProperties.class)
public class GraphQLInstrumentationConfig {

    @Bean
    public Instrumentation graphQLInstrumentation(GraphQLLimitsProperties props) {
        return new ChainedInstrumentation(List.of(
                new MaxQueryDepthInstrumentation(props.getMaxDepth()),
                new MaxQueryComplexityInstrumentation(props.getMaxComplexity())
        ));
    }
}
