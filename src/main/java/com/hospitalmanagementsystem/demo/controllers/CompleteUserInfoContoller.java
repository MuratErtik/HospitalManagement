package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.entities.UserRole;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorInformationsRequest;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorInfoResponse;
import com.hospitalmanagementsystem.demo.services.CompleteUserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/complete-information")
@RequiredArgsConstructor
public class CompleteUserInfoContoller {

    private final JwtProvider jwtProvider;

    private final CompleteUserInformationService completeUserInformationService;

    @PostMapping("/doctor/{userId}")
    public ResponseEntity<CompleteDoctorInfoResponse> completeUserInfo(@RequestHeader("Authorization") String jwt,
                                                                       @PathVariable Long userId,
                                                                       @RequestBody CompleteDoctorInformationsRequest request) throws DoctorException {

        System.out.println("88888888888888888889888875445345354");
        System.out.println(jwtProvider.getUserRoleFromToken(jwt));
        System.out.println("88888888888888888889888875445345354");

        String role = jwtProvider.getUserRoleFromToken(jwt);
        Long doctorId = jwtProvider.getUserIdFromToken(jwt);
        System.out.println("88888888888888888889888875445345354");
        System.out.println(role);
        System.out.println("88888888888888888889888875445345354");

        if(role.equals("DOCTOR") && doctorId.equals(userId) ){
            CompleteDoctorInfoResponse response = completeUserInformationService.completeDoctorInformation(request,userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
