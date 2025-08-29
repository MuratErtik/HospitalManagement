package com.hospitalmanagementsystem.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "medicines")
    private List<Prescription> prescriptions = new ArrayList<>();

    private String medicineName;

    private int dosage;

    private int duration;

    private String medicineInstructions;





}
