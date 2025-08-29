package com.hospitalmanagementsystem.demo.responses;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetAppointmentsToPatientResponse {

    private String departmentName;

    private String doctorName;

    private String note;

    private String appointmentStatus;

    private LocalDate appointmentDate;

    private LocalTime startingTime;

    private LocalTime endingTime;

    private PrescriptionToAppointmentResponse prescription;

}
