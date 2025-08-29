package com.hospitalmanagementsystem.demo.specifications;

import com.hospitalmanagementsystem.demo.entities.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentSpecifications {

    public static Specification<Appointment> appointmentSpecification(Long doctorId, LocalDate date,Long statusId) {
        return (root, query, cb) -> {

            // Appointment -> AppointmentSlot -> DoctorSchedule -> Doctor
            Join<Appointment, AppointmentSlot> appointmentSlotJoin = root.join("slot");
            Join<Appointment, AppointmentStatus> appointmentStatusJoin = root.join("status");

            Join<AppointmentSlot, DoctorSchedule> doctorScheduleJoin = appointmentSlotJoin.join("doctorSchedule");
            Join<DoctorSchedule, Doctor> doctorJoin = doctorScheduleJoin.join("doctor");
            Join<Doctor, User> userJoin = doctorJoin.join("user");


            List<Predicate> predicates = new ArrayList<>();

            if (doctorId != null) {
                predicates.add(cb.equal(userJoin.get("userId"), doctorId));
            }
            // date filtering just date
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59

                predicates.add(cb.between(
                        appointmentSlotJoin.get("startTime"),
                        startOfDay,
                        endOfDay
                ));
            }

            if (statusId != null) {
                predicates.add(cb.equal(appointmentStatusJoin.get("appointmentStatusId"), statusId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Appointment> appointmentPatientSpecification(Long userId, LocalDate date,Long statusId) {
        return (root, query, cb) -> {

            // Appointment -> AppointmentSlot -> DoctorSchedule -> Doctor
            Join<Appointment, AppointmentSlot> appointmentSlotJoin = root.join("slot");
            Join<Appointment, AppointmentStatus> appointmentStatusJoin = root.join("status");

            Join<AppointmentSlot, DoctorSchedule> doctorScheduleJoin = appointmentSlotJoin.join("doctorSchedule");
            Join<DoctorSchedule, Doctor> doctorJoin = doctorScheduleJoin.join("doctor");
            Join<Doctor, User> userJoin = doctorJoin.join("user");

            Join<Appointment, Patient> patientJoin = root.join("patient");
            Join<Patient, User> userJoin2 = patientJoin.join("user");




            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(userJoin2.get("userId"), userId));
            }
            // date filtering just date
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59

                predicates.add(cb.between(
                        appointmentSlotJoin.get("startTime"),
                        startOfDay,
                        endOfDay
                ));
            }

            if (statusId != null) {
                predicates.add(cb.equal(appointmentStatusJoin.get("appointmentStatusId"), statusId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
