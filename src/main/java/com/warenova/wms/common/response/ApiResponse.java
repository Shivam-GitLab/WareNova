package com.warenova.wms.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// ================================================
// API RESPONSE
// ================================================
// PURPOSE:
// Standard response wrapper for ALL APIs
// in WareNova WMS
//
// EVERY API returns this same structure:
// {
//   "success": true,
//   "message": "Item created successfully",
//   "data": { ... actual data ... },
//   "timestamp": "2024-01-15T10:30:00"
// }
//
// WHY STANDARD RESPONSE?
// → Frontend knows exact response structure
// → Easy to handle errors consistently
// → Professional API design
// → MNC industry standard
//
// USAGE:
// return ResponseEntity.ok(
//   ApiResponse.success("Item created", item)
// );
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// ── Don't include null fields in JSON response ──
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // ── Was the request successful? ─────────────
    // true  → operation succeeded
    // false → operation failed
    private boolean success;

    // ── Human readable message ──────────────────
    // Example: "Item created successfully"
    // Example: "Item not found"
    private String message;

    // ── Actual response data ────────────────────
    // Can be any type: Item, List<Order>, etc
    // null for error responses
    private T data;

    // ── When was this response generated ────────
    // Useful for debugging
    private LocalDateTime timestamp;

    // ── Error details (only for failures) ───────
    // null for success responses
    private String error;

    // ================================================
    // STATIC FACTORY METHODS
    // ================================================
    // Easy ways to create standard responses
    // without repeating boilerplate code
    // ================================================

    // ── SUCCESS with data ────────────────────────
    // Use when returning data to client
    // Example: get item, list orders
    public static <T> ApiResponse<T> success(
            String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ── SUCCESS without data ─────────────────────
    // Use for delete or simple confirmations
    // Example: "Item deleted successfully"
    public static <T> ApiResponse<T> success(
            String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ── ERROR response ───────────────────────────
    // Use when something goes wrong
    // Example: item not found, validation failed
    public static <T> ApiResponse<T> error(
            String message, String error) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}