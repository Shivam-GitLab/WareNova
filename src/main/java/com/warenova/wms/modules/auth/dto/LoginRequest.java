package com.warenova.wms.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// LOGIN REQUEST DTO
// ================================================
// PURPOSE:
// Captures login form data from client
//
// DTO = Data Transfer Object
// Used to receive data FROM client
// Never expose Entity directly to client!
//
// VALIDATION ANNOTATIONS:
// @NotBlank = not null AND not empty string
// message   = error message shown to client
//
// EXAMPLE REQUEST BODY:
// POST /api/auth/login
// {
//   "username": "admin",
//   "password": "admin123"
// }
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // ── Username is required ─────────────────────
    // Cannot be null or empty
    @NotBlank(message = "Username is required")
    private String username;

    // ── Password is required ─────────────────────
    // Cannot be null or empty
    @NotBlank(message = "Password is required")
    private String password;
}