package com.ep18.couriersync.backend.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extrae roles de Keycloak y los mapea a GrantedAuthorities con prefijo ROLE_.
 * - Realm roles:    claim "realm_access.roles"
 * - (Opcional) Client roles: claim "resource_access.{client}.roles"
 *
 * Ej.: "admin" -> "ROLE_ADMIN"
 */
@SuppressWarnings("unchecked")
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Usamos LinkedHashSet para evitar duplicados y mantener orden de inserción
        Set<String> rawRoles = new LinkedHashSet<>();

        // -------- 1) Realm roles (realm_access.roles) --------
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof Collection<?> roles) {
                roles.forEach(r -> {
                    if (r != null) rawRoles.add(r.toString());
                });
            }
        }

        // -------- 2) (Opcional) Client roles (resource_access.<client>.roles) --------
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess instanceof Map<?, ?> resources) {
            resources.values().forEach(val -> {
                if (val instanceof Map<?, ?> clientMap) {
                    Object clientRolesObj = clientMap.get("roles");
                    if (clientRolesObj instanceof Collection<?> clientRoles) {
                        clientRoles.forEach(r -> {
                            if (r != null) rawRoles.add(r.toString());
                        });
                    }
                }
            });
        }

        // -------- 3) Normalizar y convertir a authorities --------
        return rawRoles.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)                         // normaliza
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r) // aplica prefijo ROLE_
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());          // set inmutable sin duplicados
    }
}