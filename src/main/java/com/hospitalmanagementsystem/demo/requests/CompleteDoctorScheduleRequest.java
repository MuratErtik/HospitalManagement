package com.hospitalmanagementsystem.demo.requests;

import com.hospitalmanagementsystem.demo.entities.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class CompleteDoctorScheduleRequest {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private int slotDurationMinutes;


}
