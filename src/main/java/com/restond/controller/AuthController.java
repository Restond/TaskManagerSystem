package com.restond.controller;

import com.restond.entity.User;
import com.restond.security.JwtUtil;
import com.restond.service.EmailService;
import com.restond.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class  AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthController(EmailService emailService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtil.generateToken(authentication.getName());

            Map<String, String> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("message", "登录成功");
            response.put("token", jwt);

            User validUser = null;
            Optional<User> userOptional = userService.findByUsername(user.getUsername());

            if (userOptional.isPresent()) {
                validUser = userOptional.get();
            }

            try {
                if (validUser != null) {
                    emailService.sendLoginSuccessEmail(validUser.getEmail(), validUser.getUsername());
                    logger.info("登陆邮件已成功发送至: {}", validUser.getEmail());
                } else {
                    logger.warn("找不到用户 {}", user.getUsername());
                }
            } catch (Exception e) {
                logger.error("发送邮件失败 - 用户名: {}", user.getUsername());
                logger.error("错误: {}", e.getMessage());
                logger.debug("异常详情:", e);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "认证失败");
            errorResponse.put("message", "用户名或密码错误");
            errorResponse.put("detail", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "注册失败");
                errorResponse.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getRoles() == null || user.getRoles().trim().isEmpty()) {
                user.setRoles("ROLE_USER");
            }

            User savedUser = userService.createUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "用户注册成功");
            response.put("username", savedUser.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "注册失败");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
