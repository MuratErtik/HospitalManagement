package com.hospitalmanagementsystem.demo.services;


import com.hospitalmanagementsystem.demo.entities.Medicine;
import com.hospitalmanagementsystem.demo.exceptions.MedicineException;
import com.hospitalmanagementsystem.demo.repositories.MedicineRepository;
import com.hospitalmanagementsystem.demo.requests.InsertMedicineRequest;
import com.hospitalmanagementsystem.demo.responses.ListMedicineResponse;
import com.hospitalmanagementsystem.demo.responses.MedicineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    // insert medicine
    public MedicineResponse insertMedicine(InsertMedicineRequest request) {

        if (medicineRepository.findByMedicineNameAndDosage(request.getMedicineName(),request.getDosage()) != null) {
            throw new MedicineException("Medicine already exists with medicine name: " + request.getMedicineName()+"and dosage: " + request.getDosage());
        }

        Medicine medicine = new Medicine();

        medicine.setMedicineName(request.getMedicineName());

        medicine.setMedicineInstructions(request.getMedicineInstructions());

        medicine.setDuration(request.getDuration());

        medicine.setDosage(request.getDosage());

        medicineRepository.save(medicine);

        MedicineResponse medicineResponse = new MedicineResponse();

        medicineResponse.setMessage("Medicine inserted successfully");

        return medicineResponse;

    }

    public List<ListMedicineResponse> listMedicine(String medicineName){

        List<Medicine> medicineList;

        if (medicineName != null && !medicineName.isBlank()) {
            medicineList = medicineRepository.findByMedicineName(medicineName);
        } else {
            medicineList = medicineRepository.findAll();
        }
        return medicineList.stream().map(this::map).collect(Collectors.toList());


    }

    public ListMedicineResponse map(Medicine medicine){

        ListMedicineResponse medicineResponse = new ListMedicineResponse();

        medicine.setDosage(medicine.getDosage());

        medicineResponse.setMedicineName(medicine.getMedicineName());

        medicineResponse.setMedicineInstructions(medicine.getMedicineInstructions());

        medicineResponse.setDuration(medicine.getDuration());

        return medicineResponse;
    }





}
