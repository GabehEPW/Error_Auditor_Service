package br.com.gabrielwandscheer.errorauditorservice.application;

import br.com.gabrielwandscheer.errorauditorservice.application.dto.OrderEventDto;
import br.com.gabrielwandscheer.errorauditorservice.application.dto.OrderItemDto;
import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorAuditRecord;
import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorStatus;
import br.com.gabrielwandscheer.errorauditorservice.domain.Severity;
import br.com.gabrielwandscheer.errorauditorservice.infrastructure.persistence.ErrorAuditRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ErrorAuditService {

    private final ObjectMapper objectMapper;
    private final ErrorAuditRepository repository;

    public ErrorAuditService(ObjectMapper objectMapper, ErrorAuditRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Transactional
    public ErrorAuditRecord saveFromDlq(String payload, String queueName, String errorDetails) {
        OrderEventDto event = parsePayload(payload);
        int totalAmount = sumAmounts(event.getOrderItems());
        Severity severity = evaluateSeverity(totalAmount);

        ErrorAuditRecord record = new ErrorAuditRecord();
        record.setErrorId(UUID.randomUUID().toString());
        record.setQueueName(queueName);
        record.setPayload(payload);
        record.setTimestamp(Instant.now());
        record.setStatus(ErrorStatus.PENDING_ANALYSIS);
        record.setSeverity(severity);
        record.setErrorDetails(errorDetails);

        return repository.save(record);
    }

    private OrderEventDto parsePayload(String payload) {
        try {
            return objectMapper.readValue(payload, OrderEventDto.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Failed to parse DLQ payload", ex);
        }
    }

    private int sumAmounts(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .map(OrderItemDto::getAmount)
                .filter(amount -> amount != null)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Severity evaluateSeverity(int totalAmount) {
        if (totalAmount > 100) {
            return Severity.HIGH;
        }
        if (totalAmount >= 50) {
            return Severity.MEDIUM;
        }
        return Severity.LOW;
    }
}
