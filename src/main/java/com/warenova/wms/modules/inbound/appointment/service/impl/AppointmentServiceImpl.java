package com.warenova.wms.modules.inbound.appointment.service.impl;

import com.warenova.wms.common.enums.AppointmentStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.appointment.dto.AppointmentRequest;
import com.warenova.wms.modules.inbound.appointment.dto.AppointmentResponse;
import com.warenova.wms.modules.inbound.appointment.entity.Appointment;
import com.warenova.wms.modules.inbound.appointment.repository.AppointmentRepository;
import com.warenova.wms.modules.inbound.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentRepository
            appointmentRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE APPOINTMENT
    // ================================================
    @Override
    @Transactional
    public AppointmentResponse create(
            AppointmentRequest request) {

        log.info("Creating appointment for " +
                        "supplier: {} at dock: {}",
                request.getSupplierCode(),
                request.getDockDoor());

        // ── Generate appointment number ───────────
        String aptNumber = generateAptNumber();

        // ── Check dock availability ───────────────
        LocalDateTime slotEnd = request
                .getExpectedArrival().plusHours(2);

        List<Appointment> conflicts =
                appointmentRepository
                        .findByDockAndTimeSlot(
                                request.getWarehouseCode(),
                                request.getDockDoor(),
                                request.getExpectedArrival(),
                                slotEnd
                        );

        if (!conflicts.isEmpty()) {
            throw new WMSException(
                    "Dock " + request.getDockDoor() +
                            " is already booked for this time slot"
            );
        }

        Appointment appointment = Appointment.builder()
                .appointmentNumber(aptNumber)
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .dockDoor(request.getDockDoor().toUpperCase())
                .expectedArrival(request.getExpectedArrival())
                .asnNumber(request.getAsnNumber())
                .poNumber(request.getPoNumber())
                .vehicleNumber(request.getVehicleNumber())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .expectedPallets(request.getExpectedPallets())
                .expectedCartons(request.getExpectedCartons())
                .notes(request.getNotes())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved =
                appointmentRepository.save(appointment);

        log.info("Appointment created: {}",
                saved.getAppointmentNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        return mapToResponse(
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment",
                                        String.valueOf(id)))
        );
    }

    // ================================================
    // GET BY NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getByNumber(
            String appointmentNumber) {
        return mapToResponse(
                appointmentRepository
                        .findByAppointmentNumber(
                                appointmentNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment",
                                        appointmentNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllByWarehouse(
            String warehouseCode) {
        return appointmentRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET TODAY'S APPOINTMENTS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getTodaysAppointments(
            String warehouseCode) {

        LocalDateTime startOfDay =
                LocalDateTime.now().toLocalDate()
                        .atStartOfDay();
        LocalDateTime endOfDay =
                startOfDay.plusDays(1);

        return appointmentRepository
                .findTodaysAppointments(
                        warehouseCode,
                        startOfDay,
                        endOfDay)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // TRUCK ARRIVED
    // ================================================
    @Override
    @Transactional
    public AppointmentResponse truckArrived(Long id) {

        Appointment apt = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment", String.valueOf(id)));

        apt.setActualArrival(LocalDateTime.now());
        apt.setStatus(AppointmentStatus.IN_PROGRESS);

        log.info("Truck arrived for appointment: {}",
                apt.getAppointmentNumber());

        return mapToResponse(
                appointmentRepository.save(apt));
    }

    // ================================================
    // COMPLETE APPOINTMENT
    // ================================================
    @Override
    @Transactional
    public AppointmentResponse complete(Long id) {

        Appointment apt = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment", String.valueOf(id)));

        apt.setStatus(AppointmentStatus.COMPLETED);

        log.info("Appointment completed: {}",
                apt.getAppointmentNumber());

        return mapToResponse(
                appointmentRepository.save(apt));
    }

    // ================================================
    // CANCEL APPOINTMENT
    // ================================================
    @Override
    @Transactional
    public AppointmentResponse cancel(Long id) {

        Appointment apt = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment", String.valueOf(id)));

        apt.setStatus(AppointmentStatus.CANCELLED);

        log.info("Appointment cancelled: {}",
                apt.getAppointmentNumber());

        return mapToResponse(
                appointmentRepository.save(apt));
    }

    // ================================================
    // UPDATE APPOINTMENT
    // ================================================
    @Override
    @Transactional
    public AppointmentResponse update(
            Long id,
            AppointmentRequest request) {

        Appointment apt = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment", String.valueOf(id)));

        apt.setDockDoor(request.getDockDoor());
        apt.setExpectedArrival(
                request.getExpectedArrival());
        apt.setVehicleNumber(request.getVehicleNumber());
        apt.setDriverName(request.getDriverName());
        apt.setDriverPhone(request.getDriverPhone());
        apt.setExpectedPallets(
                request.getExpectedPallets());
        apt.setExpectedCartons(
                request.getExpectedCartons());
        apt.setNotes(request.getNotes());

        return mapToResponse(
                appointmentRepository.save(apt));
    }

    // ================================================
    // GENERATE APPOINTMENT NUMBER
    // ================================================
    private String generateAptNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "APT-" + timestamp;
    }

    // ================================================
    // MAP TO RESPONSE
    // ================================================
    private AppointmentResponse mapToResponse(
            Appointment apt) {
        AppointmentResponse response =
                modelMapper.map(apt,
                        AppointmentResponse.class);
        response.setCreatedAt(apt.getCreatedAt());
        response.setCreatedBy(apt.getCreatedBy());
        response.setUpdatedAt(apt.getUpdatedAt());
        response.setUpdatedBy(apt.getUpdatedBy());
        return response;
    }
}