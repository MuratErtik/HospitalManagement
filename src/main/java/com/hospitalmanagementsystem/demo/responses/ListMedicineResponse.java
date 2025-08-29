package com.hospitalmanagementsystem.demo.responses;

import lombok.Data;

@Data
public class ListMedicineResponse {

    private String medicineName;

    private int dosage;

    private int duration;

    private String medicineInstructions;
}
