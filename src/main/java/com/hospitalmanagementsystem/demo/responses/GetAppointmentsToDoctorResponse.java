package com.hospitalmanagementsystem.demo.responses;

import com.hospitalmanagementsystem.demo.entities.Prescription;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetAppointmentsToDoctorResponse {

    private String note;

    private String appointmentStatus;

    private LocalDate appointmentDate;

    private LocalTime startingTime;

    private LocalTime endingTime;

    private String patientName;

    private String gender;

    private LocalDate birthday;

    private String address;

    private PrescriptionToAppointmentResponse prescription;
}
