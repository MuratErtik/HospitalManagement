package com.hospitalmanagementsystem.demo.services;


import com.hospitalmanagementsystem.demo.entities.Appointment;
import com.hospitalmanagementsystem.demo.entities.AppointmentStatus;
import com.hospitalmanagementsystem.demo.entities.Medicine;
import com.hospitalmanagementsystem.demo.entities.Prescription;
import com.hospitalmanagementsystem.demo.exceptions.AppointmentException;
import com.hospitalmanagementsystem.demo.exceptions.MedicineException;
import com.hospitalmanagementsystem.demo.exceptions.PrescriptionException;
import com.hospitalmanagementsystem.demo.repositories.AppointmentRepository;
import com.hospitalmanagementsystem.demo.repositories.AppointmentStatusRepository;
import com.hospitalmanagementsystem.demo.repositories.MedicineRepository;
import com.hospitalmanagementsystem.demo.repositories.PrescriptionRepository;
import com.hospitalmanagementsystem.demo.requests.CreatePrescriptionRequest;
import com.hospitalmanagementsystem.demo.requests.InsertMedicineRequest;
import com.hospitalmanagementsystem.demo.responses.ListMedicineResponse;
import com.hospitalmanagementsystem.demo.responses.MedicineResponse;
import com.hospitalmanagementsystem.demo.responses.PrescriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    private final PrescriptionRepository prescriptionRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentStatusRepository appointmentStatusRepository;

    // insert medicine
    public MedicineResponse insertMedicine(InsertMedicineRequest request) {

        if (medicineRepository.findByMedicineNameAndDosage(request.getMedicineName(),request.getDosage()) != null) {
            throw new MedicineException("Medicine already exists with medicine name: " + request.getMedicineName()+"and dosage: " + request.getDosage());
        }

        Medicine medicine = new Medicine();

        if (request.getMedicineName() != null) {
            medicine.setMedicineName(request.getMedicineName());
        }

        if (request.getMedicineInstructions() != null) {
            medicine.setMedicineInstructions(request.getMedicineInstructions());
        }

        if (request.getDuration()>0){
            medicine.setDuration(request.getDuration());

        }

        if (request.getDosage()>0){
            medicine.setDosage(request.getDosage());

        }

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

    //create prescription and complete appointment
    public PrescriptionResponse createPrescription(Long appointmentId, CreatePrescriptionRequest request) {

        Prescription prescription = new Prescription();

        List<Medicine> prescriptionMedicines = new ArrayList<>();

        for (InsertMedicineRequest req: request.getMedicines()) {

            Medicine medicine = medicineRepository.findByMedicineNameAndDosage(req.getMedicineName(), req.getDosage());

            if (medicine == null) {
                throw new MedicineException("Medicine does not exist with medicine name: " + req.getMedicineName()+"and dosage: " + req.getDosage());

            }

            prescriptionMedicines.add(medicine);
        }


        prescription.setMedicines(prescriptionMedicines);

        prescription.setPrescriptionDate(LocalDateTime.now());

        prescription.setNotes(request.getNote());

        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        if (appointment == null) {
            throw new AppointmentException("Appointment does not exist with appointmentId: " + appointmentId);
        }

        if (appointment.getStatus().getAppointmentStatus().equals("COMPLETED")) {

            throw new PrescriptionException("Appointment already completed with appointmentId: " + appointmentId);
        }

        prescription.setAppointment(appointment);

        AppointmentStatus appointmentStatus = appointmentStatusRepository.findByAppointmentStatus("COMPLETED");

        appointment.setStatus(appointmentStatus);

        appointment.setPrescription(prescription);

        prescriptionRepository.saveAndFlush(prescription); // dive into later...

        appointmentRepository.save(appointment);

        PrescriptionResponse prescriptionResponse = new PrescriptionResponse();

        prescriptionResponse.setMessage("Prescription created successfully");

        return prescriptionResponse;

    }





}
