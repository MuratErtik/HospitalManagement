package com.hospitalmanagementsystem.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentStatusResponse {

    private Long id;

    private String status;
}
