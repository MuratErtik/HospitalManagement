package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

@Data
public class CompleteDoctorInformationsRequest {

    String hospitalPhoneNumber;

    int roomNumber;

    String specialization;

    String departmentName;
}