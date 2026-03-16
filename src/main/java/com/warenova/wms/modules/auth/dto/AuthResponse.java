package com.warenova.wms.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// AUTH RESPONSE DTO
// ================================================
// PURPOSE:
// Response sent to client after login
// Contains JWT token + user details
//
// EXAMPLE RESPONSE:
// {
//   "token": "eyJhbGci...",
//   "tokenType": "Bearer",
//   "username": "admin",
//   "fullName": "Admin User",
//   "role": "ROLE_ADMIN",
//   "warehouseCode": "WH001",
//   "expiresIn": 86400000
// }
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // ── JWT Token ────────────────────────────────
    // Client stores this and sends in every request
    // Authorization: Bearer <token>
    private String token;

    // ── Token Type ───────────────────────────────
    // Always "Bearer" for JWT
    @Builder.Default
    private String tokenType = "Bearer";

    // ── User Details ─────────────────────────────
    // Sent so client can show user info in UI
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String warehouseCode;

    // ── Token Expiry ─────────────────────────────
    // How long token is valid in milliseconds
    // 86400000 = 24 hours
    // Client uses this to show session timeout
    private Long expiresIn;
}