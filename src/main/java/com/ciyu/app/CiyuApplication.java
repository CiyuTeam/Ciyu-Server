package com.ciyu.app;

import com.ciyu.app.security.CurrentUser;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CiyuApplication {
    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(CurrentUser.class)
                .replaceParameterObjectWithClass(org.springframework.data.domain.Pageable.class, Pageable.class)
                .replaceParameterObjectWithClass(org.springframework.data.domain.PageRequest.class, Pageable.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(CiyuApplication.class, args);
    }

}
