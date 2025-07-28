package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.AuditLog;
import com.hospitalmanagementsystem.demo.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(String username, String action, String entityName, String entityId, String ipAddress, String additionalDetails) {

        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setAdditionalDetails(additionalDetails);

        auditLogRepository.save(log);
    }
}
