package br.com.gabrielwandscheer.errorauditorservice.application;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gabrielwandscheer.errorauditorservice.application.dto.OrderEventDto;
import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorAuditRecord;
import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorStatus;
import br.com.gabrielwandscheer.errorauditorservice.domain.Severity;
import br.com.gabrielwandscheer.errorauditorservice.infrastructure.persistence.ErrorAuditRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErrorAuditService {

    private final ErrorAuditRepository errorAuditRepository;
    private final ObjectMapper objectMapper;

    public void auditError(String payload, String queueName) throws JsonProcessingException {
        OrderEventDto orderEvent = objectMapper.readValue(payload, OrderEventDto.class);

        int totalItems = 0;
        if (orderEvent.getOrderItems() != null) {
            totalItems = orderEvent.getOrderItems().stream()
                .mapToInt(item -> item.getAmount())
                .sum();
        }

        Severity severity = calculateSeverity(totalItems);

        ErrorAuditRecord record = new ErrorAuditRecord();
        record.setQueueName(queueName);
        record.setPayload(payload);
        record.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        record.setStatus(ErrorStatus.PENDING_ANALYSIS);
        record.setSeverity(severity);

        errorAuditRepository.save(record);
    }

    private Severity calculateSeverity(int totalItems) {
        if (totalItems > 100) {
            return Severity.HIGH;
        } else if (totalItems >= 50) {
            return Severity.MEDIUM;
        } else {
            return Severity.LOW;
        }
    }
}
