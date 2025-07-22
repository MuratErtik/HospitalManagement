package com.hospitalmanagementsystem.demo.entities;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String action;

    private String entityName;

    private String entityId;

    private LocalDateTime timestamp;

    private String ipAddress;

    private String additionalDetails;
}

