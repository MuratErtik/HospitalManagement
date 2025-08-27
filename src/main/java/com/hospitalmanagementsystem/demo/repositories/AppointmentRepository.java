package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> , JpaSpecificationExecutor<Appointment> {

    Appointment findByAppointmentId(Long appointmentId);
}
