package com.hospitalmanagementsystem.demo.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSlotRequest {

    private LocalDate startDate;

    private LocalDate endDate;
}
