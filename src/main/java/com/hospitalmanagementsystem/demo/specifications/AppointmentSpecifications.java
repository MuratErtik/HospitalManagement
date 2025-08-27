package com.hospitalmanagementsystem.demo.specifications;

import com.hospitalmanagementsystem.demo.entities.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentSpecifications {

    public static Specification<Appointment> appointmentSpecification(Long doctorId) {
        return (root, query, cb) -> {

            // Appointment -> AppointmentSlot -> DoctorSchedule -> Doctor
            Join<Appointment, AppointmentSlot> appointmentSlotJoin = root.join("slot");
            Join<AppointmentSlot, DoctorSchedule> doctorScheduleJoin = appointmentSlotJoin.join("doctorSchedule");
            Join<DoctorSchedule, Doctor> doctorJoin = doctorScheduleJoin.join("doctor");
            Join<Doctor, User> userJoin = doctorJoin.join("user");


            List<Predicate> predicates = new ArrayList<>();

            if (doctorId != null) {
                predicates.add(cb.equal(userJoin.get("userId"), doctorId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
