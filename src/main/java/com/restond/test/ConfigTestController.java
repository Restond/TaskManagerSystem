package com.restond.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigTestController {

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping("/test")
    public Map<String, String> testConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("dbPassword_set", dbPassword != null ? "是" : "否");
        config.put("mailUsername_set", mailUsername != null ? "是" : "否");
        config.put("jwtSecret_set", jwtSecret != null ? "是" : "否");

        if (dbPassword != null) {
            config.put("dbPassword_length", String.valueOf(dbPassword.length()));
        }
        if (jwtSecret != null) {
            config.put("jwtSecret_length", String.valueOf(jwtSecret.length()));
        }

        return config;
    }
}
