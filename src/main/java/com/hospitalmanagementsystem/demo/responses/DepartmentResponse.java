package com.hospitalmanagementsystem.demo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long departmentId;

    private String departmentName;

}
