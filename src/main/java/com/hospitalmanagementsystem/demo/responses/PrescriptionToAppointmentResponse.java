package com.hospitalmanagementsystem.demo.responses;

import com.hospitalmanagementsystem.demo.entities.Appointment;
import com.hospitalmanagementsystem.demo.entities.Medicine;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrescriptionToAppointmentResponse {

    private LocalDateTime prescriptionDate;

    private String notes;

    private List<MedicineToPrescriptionResponse> medicines = new ArrayList<>();

}


