package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.requests.PatientSlotFilterRequest;
import com.hospitalmanagementsystem.demo.responses.AppointmentSlotToPatientResponse;
import com.hospitalmanagementsystem.demo.responses.DepartmentResponse;
import com.hospitalmanagementsystem.demo.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class AppointmentToPatientContoller {

    private final AppointmentService appointmentService;


    @GetMapping("/filter")
    public ResponseEntity<List<AppointmentSlotToPatientResponse>> getFilteredSlots(
            @RequestParam Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long doctorId
    ) {
        PatientSlotFilterRequest request = new PatientSlotFilterRequest();
        request.setDepartmentId(departmentId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setDoctorId(doctorId);

        List<AppointmentSlotToPatientResponse> slots = appointmentService.getFilteredSlots(request);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> departments = appointmentService.getDepartments();
        return ResponseEntity.ok(departments);
    }
}
