package com.hospitalmanagementsystem.demo.requests;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class InsertMedicineRequest {

    private String medicineName;

    private int dosage;

    private int duration;

    private String medicineInstructions;


}





