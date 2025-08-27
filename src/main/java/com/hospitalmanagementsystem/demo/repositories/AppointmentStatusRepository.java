package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Long> {

    AppointmentStatus findByAppointmentStatus(String appointmentStatus);

    AppointmentStatus findByAppointmentStatusId(Long appointmentStatusId);
}
