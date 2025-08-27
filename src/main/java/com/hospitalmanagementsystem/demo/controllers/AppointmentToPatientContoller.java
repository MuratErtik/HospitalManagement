package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.exceptions.AppointmentException;
import com.hospitalmanagementsystem.demo.requests.AppointmentNoteRequest;
import com.hospitalmanagementsystem.demo.requests.PatientSlotFilterRequest;
import com.hospitalmanagementsystem.demo.responses.*;
import com.hospitalmanagementsystem.demo.services.AppointmentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class AppointmentToPatientContoller {

    private final AppointmentService appointmentService;

    private final JwtProvider jwtProvider;


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
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam Long departmentId) {
        List<DoctorResponse> doctors = appointmentService.getDoctors(departmentId);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("{patientId}/make-appointment/{slotId}")
    public ResponseEntity<MakeAppointmentResponse> makeAppointment(@PathVariable Long patientId,@PathVariable Long slotId,@RequestHeader("Authorization") String jwt) throws AppointmentException, MessagingException {

        String role = jwtProvider.getUserRoleFromToken(jwt);
        Long patientIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if(role.equals("PATIENT") && patientIdFromToken.equals(patientId) ){
            MakeAppointmentResponse response = appointmentService.makeAppointment(slotId, patientId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("{patientId}/add-note/{appointmentId}")
    public ResponseEntity<MakeAppointmentResponse> addNote(@PathVariable Long patientId, @PathVariable Long appointmentId, @RequestHeader("Authorization") String jwt, @RequestBody AppointmentNoteRequest request) throws AppointmentException, MessagingException {

        String role = jwtProvider.getUserRoleFromToken(jwt);
        Long patientIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if(role.equals("PATIENT") && patientIdFromToken.equals(patientId) ){
            MakeAppointmentResponse response = appointmentService.addNote(appointmentId, patientId,request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/status")
    public ResponseEntity<List<AppointmentStatusResponse>> getAllAppointmentStatus() {
        List<AppointmentStatusResponse> status = appointmentService.getAllAppointmentStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("{patientId}/change-status/{appointmentId}")
    public ResponseEntity<MakeAppointmentResponse> changeStatus(@PathVariable Long patientId, @PathVariable Long appointmentId, @RequestHeader("Authorization") String jwt,@RequestParam Long statusId) throws AppointmentException {

        String role = jwtProvider.getUserRoleFromToken(jwt);
        Long patientIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if(patientIdFromToken.equals(patientId) ){
            MakeAppointmentResponse response = appointmentService.changeAppointmentStatus(appointmentId, patientId,statusId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }


}
