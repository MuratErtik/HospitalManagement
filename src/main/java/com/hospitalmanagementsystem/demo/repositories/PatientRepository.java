package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.Patient;
import com.hospitalmanagementsystem.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Patient findPatientByUser(User user);
}
