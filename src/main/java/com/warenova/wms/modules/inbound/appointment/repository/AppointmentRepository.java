package com.warenova.wms.modules.inbound.appointment.repository;

import com.warenova.wms.common.enums.AppointmentStatus;
import com.warenova.wms.modules.inbound.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentNumber(
            String appointmentNumber);

    boolean existsByAppointmentNumber(
            String appointmentNumber);

    List<Appointment> findByWarehouseCode(
            String warehouseCode);

    List<Appointment> findByWarehouseCodeAndStatus(
            String warehouseCode,
            AppointmentStatus status);

    // ── Find by dock door and date range ─────────
    // Check if dock is available for slot
    @Query("SELECT a FROM Appointment a WHERE " +
            "a.warehouseCode = :warehouseCode AND " +
            "a.dockDoor = :dockDoor AND " +
            "a.status NOT IN ('COMPLETED','CANCELLED') AND " +
            "a.expectedArrival BETWEEN :start AND :end")
    List<Appointment> findByDockAndTimeSlot(
            @Param("warehouseCode") String warehouseCode,
            @Param("dockDoor") String dockDoor,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // ── Today's appointments ──────────────────────
    @Query("SELECT a FROM Appointment a WHERE " +
            "a.warehouseCode = :warehouseCode AND " +
            "a.expectedArrival BETWEEN :start AND :end")
    List<Appointment> findTodaysAppointments(
            @Param("warehouseCode") String warehouseCode,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<Appointment> findBySupplierCode(
            String supplierCode);
}