package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.entities.Appointment;
import com.hospitalmanagementsystem.demo.exceptions.AppointmentException;
import com.hospitalmanagementsystem.demo.responses.AppointmentStatusResponse;
import com.hospitalmanagementsystem.demo.responses.GetAppointmentsToDoctorResponse;
import com.hospitalmanagementsystem.demo.responses.MakeAppointmentResponse;
import com.hospitalmanagementsystem.demo.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/doctor")
@RequiredArgsConstructor
public class AppointmentToDoctorController {

    private final AppointmentService appointmentService;

    private final JwtProvider jwtProvider;

    @GetMapping("/status")
    public ResponseEntity<List<AppointmentStatusResponse>> getAllAppointmentStatus() {
        List<AppointmentStatusResponse> status = appointmentService.getAllAppointmentStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("{doctorId}/change-status/{appointmentId}")
    public ResponseEntity<MakeAppointmentResponse> changeStatus(@PathVariable Long doctorId, @PathVariable Long appointmentId, @RequestHeader("Authorization") String jwt, @RequestParam Long statusId) throws AppointmentException {

        Long doctorIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if(doctorIdFromToken.equals(doctorId) ){
            MakeAppointmentResponse response = appointmentService.changeAppointmentStatus(appointmentId, doctorId,statusId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }



    @GetMapping("{doctorId}/get-all-appointment")
    public ResponseEntity<List<GetAppointmentsToDoctorResponse>> getAllAppointment (@PathVariable Long doctorId,
                                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                                    @RequestHeader("Authorization") String jwt
                                                                                    /*@RequestParam Long statusId) throws AppointmentException */){

        Long doctorIdFromToken = jwtProvider.getUserIdFromToken(jwt);

        if(doctorIdFromToken.equals(doctorId) ){
            List<GetAppointmentsToDoctorResponse> response = appointmentService.getAppointmentsToDoctor(doctorId,date);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
