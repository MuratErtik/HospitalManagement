package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.Department;
import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.repositories.DepartmentRepository;
import com.hospitalmanagementsystem.demo.repositories.DoctorRepository;
import com.hospitalmanagementsystem.demo.repositories.PatientRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorInformationsRequest;
import com.hospitalmanagementsystem.demo.requests.CompletePatientInformationsRequest;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorInfoResponse;
import com.hospitalmanagementsystem.demo.responses.CompletePatientInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompleteUserInformationService {

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    //complete doctor info
    //get doctor request for fill up the information
    public CompleteDoctorInfoResponse completeDoctorInformation(CompleteDoctorInformationsRequest doctor, Long userId) throws DoctorException {

        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new DoctorException("User not found with id " + userId);
        }

        Doctor doctorToUpdate = doctorRepository.findDoctorByUser(user);

        if (doctorToUpdate == null) {
            throw new DoctorException("Doctor not found with id " + userId);
        }

        doctorToUpdate.setHospitalPhoneNo(doctor.getHospitalPhoneNumber());

        doctorToUpdate.setRoomNumber(doctor.getRoomNumber());

        doctorToUpdate.setSpecialization(doctor.getSpecialization());

        Department department = departmentRepository.findByDepartmentName(doctor.getDepartmentName());

        if (department == null) {
            throw new DoctorException("Department not found with name " + doctor.getDepartmentName());
        }

        doctorToUpdate.setDepartment(department);

        doctorRepository.save(doctorToUpdate);

        CompleteDoctorInfoResponse completeDoctorInfoResponse = new CompleteDoctorInfoResponse();

        completeDoctorInfoResponse.setMessage("Successfully completed user information and user id " + userId);

        return completeDoctorInfoResponse;

    }


    //complete patient info



}
