package com.warenova.wms.modules.auth.service;

import com.warenova.wms.modules.auth.dto.AuthResponse;
import com.warenova.wms.modules.auth.dto.LoginRequest;
import com.warenova.wms.modules.auth.dto.RegisterRequest;

// ================================================
// AUTH SERVICE INTERFACE
// ================================================
// PURPOSE:
// Defines the contract for auth operations
//
// WHY INTERFACE + IMPL?
// → Clean separation of what vs how
// → Easy to swap implementation later
// → Easy to mock in unit tests
// → MNC standard practice
//
// AuthService     = WHAT to do (contract)
// AuthServiceImpl = HOW to do it (logic)
// ================================================

public interface AuthService {

    // ── Login user and return JWT token ──────────
    AuthResponse login(LoginRequest request);

    // ── Register new user ────────────────────────
    AuthResponse register(RegisterRequest request);
}