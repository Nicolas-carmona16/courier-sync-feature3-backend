package com.ep18.couriersync.backend.config.graphql;

import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.DomainException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.execution.GraphQlErrorBuilder;
import org.springframework.stereotype.Component;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Slf4j
@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        // 1) Excepciones de dominio (controladas)
        if (ex instanceof DomainException de) {
            ErrorType type = ErrorType.BAD_REQUEST;
            if (de instanceof NotFoundException) type = ErrorType.NOT_FOUND;
            if (de instanceof ConflictException) type = ErrorType.BAD_REQUEST; // o ErrorType.CONFLICT si prefieres
            if (de instanceof ValidationException) type = ErrorType.BAD_REQUEST;

            // Log de advertencia con path; evita PII en el mensaje
            log.warn("GraphQL domain error on {}: {} ({})",
                    env.getExecutionStepInfo().getPath(), de.getMessage(), de.getCode());

            return GraphQlErrorBuilder.newError(env)
                    .errorType(type)
                    .message(de.getMessage())
                    .extension("code", de.getCode())       // p.ej. VALIDATION_ERROR, NOT_FOUND, CONFLICT
                    .build();
        }

        // 2) Otras excepciones (no controladas) → mensaje genérico
        log.error("GraphQL internal error on {}: {}",
                env.getExecutionStepInfo().getPath(), ex.toString());

        return GraphQlErrorBuilder.newError(env)
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Error interno. Intenta más tarde")
                .extension("code", "INTERNAL_ERROR")
                .build();
    }
}