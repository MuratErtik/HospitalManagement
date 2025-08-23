package com.hospitalmanagementsystem.demo.controllers;


import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorInformationsRequest;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorScheduleRequest;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorInfoResponse;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorScheduleResponse;
import com.hospitalmanagementsystem.demo.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/doctor-schedule")
@RequiredArgsConstructor
public class AppointmentController {

    private final JwtProvider jwtProvider;

    private final AppointmentService appointmentService;

    @PostMapping("/complete/{userId}")
    public ResponseEntity<CompleteDoctorScheduleResponse> complete(@RequestHeader("Authorization") String jwt,
                                                                   @PathVariable Long userId,
                                                                   @RequestBody CompleteDoctorScheduleRequest request) throws DoctorException {

        String role = jwtProvider.getUserRoleFromToken(jwt);
        Long doctorId = jwtProvider.getUserIdFromToken(jwt);

        if(role.equals("DOCTOR") && doctorId.equals(userId) ){
            CompleteDoctorScheduleResponse response = appointmentService.completeDoctorScheduleResponse(request, userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
