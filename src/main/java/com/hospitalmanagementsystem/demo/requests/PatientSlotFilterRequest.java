package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientSlotFilterRequest {

    private Long departmentId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long doctorId;

}
