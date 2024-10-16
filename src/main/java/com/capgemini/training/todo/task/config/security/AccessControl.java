package com.capgemini.training.todo.task.config.security;

import org.springframework.stereotype.Component;

@Component("Roles")
public class AccessControl {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MAINTAINER = "MAINTAINER";
}
