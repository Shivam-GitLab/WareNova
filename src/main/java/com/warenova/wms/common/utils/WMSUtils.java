package com.warenova.wms.common.utils;

import com.warenova.wms.common.constants.WMSConstants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// ================================================
// WMS UTILS
// ================================================
// PURPOSE:
// Utility/helper methods used across
// entire WareNova WMS application
//
// All methods are STATIC → no object needed
// WMSUtils.generatePONumber() ← call like this
// ================================================
public final class WMSUtils {

    // ── Prevent instantiation ────────────────────
    private WMSUtils() { }

    // ================================================
    // GENERATE PO NUMBER
    // ================================================
    // Format: PO-2024-000001
    // ================================================
    public static String generatePONumber() {
        String year = String.valueOf(
                LocalDateTime.now().getYear());
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(8);
        return WMSConstants.PO_PREFIX + year + "-" + unique;
    }

    // ================================================
    // GENERATE ASN NUMBER
    // ================================================
    // Format: ASN-2024-000001
    // ================================================
    public static String generateASNNumber() {
        String year = String.valueOf(
                LocalDateTime.now().getYear());
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(8);
        return WMSConstants.ASN_PREFIX + year + "-" + unique;
    }

    // ================================================
    // GENERATE WAVE NUMBER
    // ================================================
    // Format: WAVE-000001
    // ================================================
    public static String generateWaveNumber() {
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(7);
        return WMSConstants.WAVE_PREFIX + unique;
    }

    // ================================================
    // GENERATE LPN NUMBER
    // ================================================
    // LPN = License Plate Number
    // Unique ID for each pallet or carton
    // Format: LPN-000001
    // ================================================
    public static String generateLPNNumber() {
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(7);
        return WMSConstants.LPN_PREFIX + unique;
    }

    // ================================================
    // GENERATE SALES ORDER NUMBER
    // ================================================
    // Format: SO-2024-000001
    // ================================================
    public static String generateSONumber() {
        String year = String.valueOf(
                LocalDateTime.now().getYear());
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(8);
        return WMSConstants.SO_PREFIX + year + "-" + unique;
    }

    // ================================================
    // GENERATE SHIPMENT NUMBER
    // ================================================
    // Format: SHP-2024-000001
    // ================================================
    public static String generateShipmentNumber() {
        String year = String.valueOf(
                LocalDateTime.now().getYear());
        String unique = String.valueOf(
                System.currentTimeMillis()).substring(8);
        return WMSConstants.SHP_PREFIX + year + "-" + unique;
    }

    // ================================================
    // FORMAT DATE TIME
    // ================================================
    // Returns: "2024-01-15 10:30:00"
    // ================================================
    public static String formatDateTime(
            LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss"));
    }
}