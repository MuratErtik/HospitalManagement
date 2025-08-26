package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Department;
import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Doctor findDoctorByUser(User user);

    Doctor findDoctorByDoctorId(Long id);

    List<Doctor> findDoctorByDepartment(Department department);
}
