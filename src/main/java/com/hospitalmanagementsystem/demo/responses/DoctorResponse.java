package com.hospitalmanagementsystem.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long doctorId;

    private String doctorName;

    private String doctorLastName;
}
