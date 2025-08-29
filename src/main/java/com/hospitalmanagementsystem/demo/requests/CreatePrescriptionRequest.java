package com.hospitalmanagementsystem.demo.requests;

import com.hospitalmanagementsystem.demo.entities.Medicine;

import lombok.Data;


import java.util.List;

@Data
public class CreatePrescriptionRequest {

    String note;

    List<InsertMedicineRequest> medicines;


}

