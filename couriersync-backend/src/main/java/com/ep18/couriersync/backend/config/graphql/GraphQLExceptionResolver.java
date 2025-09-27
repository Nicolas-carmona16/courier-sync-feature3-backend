package com.ep18.couriersync.backend.config.graphql;

import com.ep18.couriersync.backend.common.exception.ConflictException;
import com.ep18.couriersync.backend.common.exception.DomainException;
import com.ep18.couriersync.backend.common.exception.NotFoundException;
import com.ep18.couriersync.backend.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        // Bean Validation (@Valid / @Validated)
        if (ex instanceof ConstraintViolationException cve) {
            String message = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(message)
                    .extensions(Map.of("code", "VALIDATION_ERROR"))
                    .build();
        }
        if (ex instanceof MethodArgumentNotValidException manve) {
            String message = manve.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(message)
                    .extensions(Map.of("code", "VALIDATION_ERROR"))
                    .build();
        }

        // Excepciones de dominio (controladas)
        if (ex instanceof DomainException de) {
            ErrorType type = ErrorType.BAD_REQUEST;
            if (de instanceof NotFoundException)  type = ErrorType.NOT_FOUND;
            if (de instanceof ConflictException)  type = ErrorType.BAD_REQUEST;
            if (de instanceof ValidationException) type = ErrorType.BAD_REQUEST;

            log.warn("GraphQL domain error on {}: {} ({})",
                    env.getExecutionStepInfo().getPath(), de.getMessage(), de.getCode());

            return GraphqlErrorBuilder.newError(env)
                    .errorType(type)
                    .message(de.getMessage())
                    .extensions(Map.of("code", de.getCode()))
                    .build();
        }

        // Otras excepciones (no controladas)
        log.error("GraphQL internal error on {}: {}", env.getExecutionStepInfo().getPath(), ex.toString());

        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Error interno. Intenta más tarde")
                .extensions(Map.of("code", "INTERNAL_ERROR"))
                .build();
    }
}