package com.warenova.wms.common.exception;

import lombok.Getter;

// ================================================
// WMS EXCEPTION
// ================================================
// PURPOSE:
// Base exception for ALL WareNova WMS exceptions
// All other custom exceptions extend this
//
// WHY CUSTOM EXCEPTION?
// → Give meaningful error messages
// → Separate WMS errors from system errors
// → Easy to catch all WMS errors in one place
//
// USAGE:
// throw new WMSException("Invalid operation");
// ================================================
@Getter
public class WMSException extends RuntimeException {

    // ── Get status code ──────────────────────────
    // ── Store HTTP status code ───────────────────
    // Default 400 Bad Request
    private final int statusCode;

    // ── Constructor with message only ────────────
    // Default status = 400 Bad Request
    public WMSException(String message) {
        super(message);
        this.statusCode = 400;
    }

    // ── Constructor with message and status ──────
    // Use when you need specific HTTP status
    public WMSException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}