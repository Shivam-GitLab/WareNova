package com.warenova.wms.common.exception;

import com.warenova.wms.common.response.ApiError;
import com.warenova.wms.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// ================================================
// GLOBAL EXCEPTION HANDLER
// ================================================
// PURPOSE:
// Catches ALL exceptions thrown anywhere in
// the application and returns proper
// JSON error responses to the client
//
// WITHOUT this: Spring returns ugly HTML errors
// WITH this:    Returns clean JSON responses
//
// @RestControllerAdvice → intercepts all
// exceptions from all controllers
//
// HOW IT WORKS:
// 1. Exception thrown in Service or Controller
// 2. Spring looks for matching @ExceptionHandler
// 3. Found handler runs and returns JSON response
// 4. Client receives clean error response
// ================================================
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================================
    // HANDLE VALIDATION ERRORS
    // ================================================
    // Triggered when @Valid fails on request body
    // Example: @NotBlank, @NotNull, @Email fails
    // Returns: 400 with list of field errors
    // ================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Collect all field validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                // Format: "fieldName: error message"
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // ================================================
    // HANDLE RESOURCE NOT FOUND
    // ================================================
    // Triggered when item/order/location not found
    // Returns: 404 Not Found
    // ================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(), "NOT_FOUND"
                ));
    }

    // ================================================
    // HANDLE DUPLICATE RESOURCE
    // ================================================
    // Triggered when creating duplicate records
    // Returns: 409 Conflict
    // ================================================
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleDuplicateResource(
            DuplicateResourceException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(), "DUPLICATE"
                ));
    }

    // ================================================
    // HANDLE INSUFFICIENT STOCK
    // ================================================
    // Triggered when stock is not enough
    // Returns: 400 Bad Request
    // ================================================
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleInsufficientStock(
            InsufficientStockException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(), "INSUFFICIENT_STOCK"
                ));
    }

    // ================================================
    // HANDLE GENERAL WMS EXCEPTION
    // ================================================
    // Triggered for any other WMS business error
    // Returns: status from exception
    // ================================================
    @ExceptionHandler(WMSException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleWMSException(WMSException ex) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ApiResponse.error(
                        ex.getMessage(), "WMS_ERROR"
                ));
    }

    // ================================================
    // HANDLE BAD CREDENTIALS
    // ================================================
    // Triggered when wrong username or password
    // Returns: 401 Unauthorized
    // ================================================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        "Invalid username or password",
                        "UNAUTHORIZED"
                ));
    }

    // ================================================
    // HANDLE ACCESS DENIED
    // ================================================
    // Triggered when user accesses API
    // without required role/permission
    // Returns: 403 Forbidden
    // ================================================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(
                        "You don't have permission to access this resource",
                        "FORBIDDEN"
                ));
    }

    // ================================================
    // HANDLE ALL OTHER EXCEPTIONS
    // ================================================
    // Fallback handler for any unexpected error
    // Returns: 500 Internal Server Error
    // ================================================
    // In GlobalExceptionHandler.java (Phase 1)
// This controls ALL error responses:

// ── Never exposes stack trace ────────────────
// Returns only clean message to client ✅

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAllExceptions(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Something went wrong. Please try again.",
                        ex.getMessage()   // ← controlled message
                        //   no stack trace ✅
                ));
    }
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleAllExceptions(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Something went wrong. Please try again.",
                        ex.getMessage()
                ));
    }*/
}