package com.warenova.wms.modules.auth.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.auth.dto.AuthResponse;
import com.warenova.wms.modules.auth.dto.LoginRequest;
import com.warenova.wms.modules.auth.dto.RegisterRequest;
import com.warenova.wms.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ================================================
// AUTH CONTROLLER
// ================================================
// PURPOSE:
// Exposes REST APIs for authentication
// Login and Register endpoints
//
// BASE URL: /api/auth
// These are PUBLIC endpoints (no token needed)
// Configured in SecurityConfig.permitAll()
//
// @RestController = @Controller + @ResponseBody
// Automatically converts return to JSON
//
// @RequestMapping = base URL for all methods
//
// @Tag = Swagger UI grouping label
// Shows "Authentication" section in Swagger
// ================================================

@RestController
@RequestMapping("/api/auth")

@Tag(
        name = "Authentication",
        description = "Login and Register APIs"
)
@RequiredArgsConstructor
public class AuthController {

    // ── Auth business logic ──────────────────────
    private final AuthService authService;

    // ================================================
    // LOGIN API
    // ================================================
    // URL:    POST /api/auth/login
    // ACCESS: Public (no token needed)
    //
    // REQUEST BODY:
    // {
    //   "username": "admin",
    //   "password": "Admin@1234"
    // }
    //
    // SUCCESS RESPONSE: 200 OK
    // {
    //   "success": true,
    //   "message": "Login successful",
    //   "data": {
    //     "token": "eyJhbGci...",
    //     "username": "admin",
    //     "role": "ROLE_ADMIN"
    //   }
    // }
    //
    // FAILURE RESPONSE: 401 Unauthorized
    // {
    //   "success": false,
    //   "message": "Invalid username or password"
    // }
    // ================================================
    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Login with username and password to get JWT token"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        // @Valid → triggers validation annotations
        // If validation fails → GlobalExceptionHandler
        // returns 400 with error details

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }

    // ================================================
    // REGISTER API
    // ================================================
    // URL:    POST /api/auth/register
    // ACCESS: Public (no token needed)
    //
    // REQUEST BODY:
    // {
    //   "username": "john.doe",
    //   "password": "John@1234",
    //   "email": "john@warenova.com",
    //   "fullName": "John Doe",
    //   "role": "ROLE_WAREHOUSE",
    //   "warehouseCode": "WH001"
    // }
    //
    // SUCCESS RESPONSE: 201 Created
    // {
    //   "success": true,
    //   "message": "User registered successfully",
    //   "data": {
    //     "token": "eyJhbGci...",
    //     "username": "john.doe",
    //     "role": "ROLE_WAREHOUSE"
    //   }
    // }
    // ================================================
    @PostMapping("/register")
    @Operation(
            summary = "Register New User",
            description = "Register a new WareNova WMS user"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        // 201 Created = new resource created
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "User registered successfully", response
                ));
    }
}
//```
//
//        ---
//
//        ## ✅ Phase 3 Checklist
//```
//AUTH MODULE FILES:
//        ──────────────────────────────────────────────
//        □ User.java               ← auth/entity/
//        □ UserRepository.java     ← auth/repository/
//        □ LoginRequest.java       ← auth/dto/
//        □ RegisterRequest.java    ← auth/dto/
//        □ AuthResponse.java       ← auth/dto/
//        □ AuthService.java        ← auth/service/
//        □ AuthServiceImpl.java    ← auth/service/
//        □ AuthController.java     ← auth/controller/
//
//TOTAL → 8 files ✅
//        ```
//
//        ---
//
//        ## ✅ After Creating All Files — Test in Swagger
//```
//STEP 1 → Run WareNovaApplication.java
//STEP 2 → Open browser
//http://localhost:8080/swagger-ui/index.html
//STEP 3 → Find POST /api/auth/register
//STEP 4 → Register admin user:
//        {
//        "username": "admin",
//        "password": "Admin@1234",
//        "email": "admin@warenova.com",
//        "fullName": "Admin User",
//        "role": "ROLE_ADMIN",
//        "warehouseCode": "WH001"
//        }
//STEP 5 → Copy token from response
//STEP 6 → Click Authorize button (top right)
//STEP 7 → Paste token → Authorize
//STEP 8 → Test POST /api/auth/login
//         → Should return token ✅