package com.hospitalmanagementsystem.demo.repositories;

import com.hospitalmanagementsystem.demo.entities.AppointmentSlot;
import com.hospitalmanagementsystem.demo.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long>, JpaSpecificationExecutor<AppointmentSlot> {

    boolean existsByDoctorScheduleAndStartTimeLessThanAndEndTimeGreaterThan(
            DoctorSchedule doctorSchedule,
            LocalDateTime newEndTime,
            LocalDateTime newStartTime
    );

    boolean existsByDoctorScheduleAndStartTime(
            DoctorSchedule doctorSchedule,
            LocalDateTime startTime
    );

    AppointmentSlot findAppointmentSlotBySlotId(Long slotId);
}
