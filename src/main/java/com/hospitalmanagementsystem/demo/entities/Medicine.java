package com.hospitalmanagementsystem.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "prescription_id", nullable = false)
//    private Prescription prescription;

    private String medicineName;

    private int dosage;

    private int duration;

    private String medicineInstructions;




}
