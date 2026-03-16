package com.warenova.wms.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// REGISTER REQUEST DTO
// ================================================
// PURPOSE:
// Captures new user registration data
//
// VALIDATION:
// All fields validated before hitting service
// If any validation fails →
// GlobalExceptionHandler returns 400 with errors
//
// EXAMPLE REQUEST BODY:
// POST /api/auth/register
// {
//   "username": "john.doe",
//   "password": "John@1234",
//   "email": "john@warenova.com",
//   "fullName": "John Doe",
//   "role": "ROLE_WAREHOUSE",
//   "phone": "9876543210",
//   "warehouseCode": "WH001"
// }
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // ================================================
    // USERNAME
    // ================================================
    // 3-50 characters
    // Only letters, numbers, dots, underscores
    // ================================================
    @NotBlank(message = "Username is required")
    @Size(
            min = 3,
            max = 50,
            message = "Username must be 3-50 characters"
    )
    private String username;

    // ================================================
    // PASSWORD
    // ================================================
    // Min 8 characters
    // Must have uppercase, lowercase, digit, special
    // ================================================
    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            message = "Password must be at least 8 characters"
    )
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have uppercase, lowercase, number and special character"
    )
    private String password;

    // ================================================
    // EMAIL
    // ================================================
    // Valid email format required
    // ================================================
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // ================================================
    // FULL NAME
    // ================================================
    @NotBlank(message = "Full name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Full name must be 2-100 characters"
    )
    private String fullName;

    // ================================================
    // ROLE
    // ================================================
    // Must be one of UserRole enum values
    // Example: ROLE_ADMIN, ROLE_WAREHOUSE
    // ================================================
    @NotBlank(message = "Role is required")
    private String role;

    // ================================================
    // PHONE (OPTIONAL)
    // ================================================
    private String phone;

    // ================================================
    // WAREHOUSE CODE (OPTIONAL)
    // ================================================
    // Which warehouse user belongs to
    // Example: WH001
    // ================================================
    private String warehouseCode;
}