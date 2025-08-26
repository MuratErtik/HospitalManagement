package com.hospitalmanagementsystem.demo.specifications;

import com.hospitalmanagementsystem.demo.entities.*;
import com.hospitalmanagementsystem.demo.requests.PatientSlotFilterRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentSlotSpecifications {

    public static Specification<AppointmentSlot> byFilters(PatientSlotFilterRequest request) {

        return (root,query,cb) -> {

            Join<AppointmentSlot, DoctorSchedule> schedule = root.join("doctorSchedule");
            Join<DoctorSchedule, Doctor> doctor = schedule.join("doctor");

            Join<Doctor, Department> department = doctor.join("department");

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(department.get("departmentId"), request.getDepartmentId()));

            //date range control
            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(
                        cb.between(root.get("startTime"),
                                request.getStartDate().atStartOfDay(),
                                request.getEndDate().atTime(LocalTime.MAX))
                );
            }

            // doctorId kontrolü
            if (request.getDoctorId() != null) {
                predicates.add(cb.equal(doctor.get("doctorId"), request.getDoctorId()));
            }

            // just (booked = false) slots
            predicates.add(cb.isFalse(root.get("booked")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

