package com.homework.basic.domain.entity;

import lombok.Getter;

@Getter
public enum UserRole {

    USER(Authority.USER), ADMIN(Authority.ADMIN);

    private final String authority;

    UserRole(String role) {
        authority = role;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
