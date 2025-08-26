package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    DoctorSchedule findDoctorScheduleByDoctor(Doctor doctor);

}
