package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findByAppointmentId(Long appointmentId);
}
