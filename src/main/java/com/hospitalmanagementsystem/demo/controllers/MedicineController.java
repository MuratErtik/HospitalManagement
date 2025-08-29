package com.hospitalmanagementsystem.demo.controllers;

import com.hospitalmanagementsystem.demo.config.JwtProvider;
import com.hospitalmanagementsystem.demo.exceptions.AuthException;
import com.hospitalmanagementsystem.demo.exceptions.MedicineException;
import com.hospitalmanagementsystem.demo.requests.CreatePrescriptionRequest;
import com.hospitalmanagementsystem.demo.requests.InsertMedicineRequest;
import com.hospitalmanagementsystem.demo.requests.SignupRequest;
import com.hospitalmanagementsystem.demo.responses.*;
import com.hospitalmanagementsystem.demo.services.MedicineService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medicine")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    private final JwtProvider jwtProvider;

    @PostMapping("/insert")
    public ResponseEntity<MedicineResponse> insertMedicine(@RequestBody InsertMedicineRequest request,@RequestHeader("Authorization") String jwt) throws MedicineException {

        String role = jwtProvider.getUserRoleFromToken(jwt);

        if(role.equals("DOCTOR")){
            MedicineResponse response = medicineService.insertMedicine(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ListMedicineResponse>> insertMedicine(@RequestHeader("Authorization") String jwt,@RequestParam(required = false) String medicineName) throws MedicineException {

        String role = jwtProvider.getUserRoleFromToken(jwt);

        if(role.equals("DOCTOR")){
            List<ListMedicineResponse> response = medicineService.listMedicine(medicineName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }
    @PostMapping("/addPrescription/{appId}")
    public ResponseEntity<PrescriptionResponse> addPrescription(@RequestHeader("Authorization") String jwt,@RequestBody CreatePrescriptionRequest req,@PathVariable Long appId) throws MedicineException {

        String role = jwtProvider.getUserRoleFromToken(jwt);
        if(role.equals("DOCTOR")){
            PrescriptionResponse response = medicineService.createPrescription(appId,req);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
