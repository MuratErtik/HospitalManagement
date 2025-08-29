package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Medicine findByMedicineNameAndDosage(String medicineName, int dosage);

    List<Medicine> findByMedicineName(String medicineName);
}
