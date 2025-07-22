package com.hospitalmanagementsystem.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "patiences")
public class Patience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long patienceId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String gender;

    private LocalDate birthDate;

    private String address;

    private String emergencyContact;




}
