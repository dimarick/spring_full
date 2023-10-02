package ru.students.spring_full.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import ru.students.spring_full.entity.Audit;
import ru.students.spring_full.repository.AuditRepository;
import ru.students.spring_full.repository.UserRepository;

import java.time.OffsetDateTime;

@Slf4j
@Service
public class AuditService {
    private final UserRepository userRepository;
    private final AuditRepository auditRepository;

    AuditService(UserRepository userRepository, AuditRepository auditRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    public void log(String message, Object ...jsonData) {
        var sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        var owner = userRepository.findByEmail(userName);
        String data = null;
        try {
            data = new ObjectMapper().writeValueAsString(jsonData);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        auditRepository.save(new Audit(null, message, data, sessionId, owner, OffsetDateTime.now()));

        log.info(message, jsonData);
    }
}
