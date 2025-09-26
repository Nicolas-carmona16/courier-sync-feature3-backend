package com.ep18.couriersync.backend.config.graphql;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.graphql.limits")
public class GraphQLLimitsProperties {
    private int maxDepth = 15;
    private int maxComplexity = 500;

    public int getMaxDepth() { return maxDepth; }
    public void setMaxDepth(int maxDepth) { this.maxDepth = maxDepth; }

    public int getMaxComplexity() { return maxComplexity; }
    public void setMaxComplexity(int maxComplexity) { this.maxComplexity = maxComplexity; }
}
