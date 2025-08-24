package com.hospitalmanagementsystem.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_schedule_id", nullable = false)
    private DoctorSchedule doctorSchedule;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean booked;  // is slot free to open or not

    @OneToOne(mappedBy = "slot")
    private Appointment appointment;
}
