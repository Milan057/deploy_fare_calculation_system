package com.mdbackend.mdbackend.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class CsrfController {

    @GetMapping("/csrf-token")
    public void csrfToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println(csrfToken);
        if (csrfToken != null) {
            response.setHeader("X-CSRF-TOKEN", csrfToken.getToken());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "CSRF token not found");
        }
    }
}
