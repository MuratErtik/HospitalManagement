package com.hospitalmanagementsystem.demo.requests;

import com.hospitalmanagementsystem.demo.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompletePatientInformationsRequest {


    private String gender;

    private LocalDate birthDate;

    private String address;

    private String emergencyContact;
}
