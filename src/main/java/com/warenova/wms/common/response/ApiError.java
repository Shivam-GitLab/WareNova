package com.warenova.wms.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

// ================================================
// API ERROR
// ================================================
// PURPOSE:
// Detailed error response for validation failures
// Returns field level errors to frontend
//
// EXAMPLE RESPONSE:
// {
//   "status": 400,
//   "message": "Validation failed",
//   "errors": [
//     "itemName: must not be blank",
//     "sku: must not be null"
//   ],
//   "path": "/api/master/items",
//   "timestamp": "2024-01-15T10:30:00"
// }
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    // ── HTTP status code ─────────────────────────
    // 400 = Bad Request
    // 404 = Not Found
    // 500 = Internal Server Error
    private int status;

    // ── Short error message ──────────────────────
    private String message;

    // ── List of field validation errors ─────────
    // ["itemName: must not be blank", "sku: required"]
    private List<String> errors;

    // ── Which API endpoint caused error ─────────
    // Example: "/api/master/items"
    private String path;

    // ── When did error occur ─────────────────────
    private LocalDateTime timestamp;
}