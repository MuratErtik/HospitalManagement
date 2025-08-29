package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class ChangeDoctorScheduleRequest {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private int slotDurationMinutes;

}
