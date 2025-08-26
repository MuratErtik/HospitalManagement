package com.hospitalmanagementsystem.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AppointmentSlotToPatientResponse {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String doctorFullName;

    private String departmentName;

}
