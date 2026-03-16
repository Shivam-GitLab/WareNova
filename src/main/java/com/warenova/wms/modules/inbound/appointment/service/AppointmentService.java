package com.warenova.wms.modules.inbound.appointment.service;

import com.warenova.wms.modules.inbound.appointment.dto.AppointmentRequest;
import com.warenova.wms.modules.inbound.appointment.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(
            AppointmentRequest request);

    AppointmentResponse getById(Long id);

    AppointmentResponse getByNumber(
            String appointmentNumber);

    List<AppointmentResponse> getAllByWarehouse(
            String warehouseCode);

    List<AppointmentResponse> getTodaysAppointments(
            String warehouseCode);

    // ── Truck arrives ────────────────────────────
    AppointmentResponse truckArrived(Long id);

    // ── Complete appointment ─────────────────────
    AppointmentResponse complete(Long id);

    // ── Cancel appointment ───────────────────────
    AppointmentResponse cancel(Long id);

    AppointmentResponse update(
            Long id, AppointmentRequest request);
}