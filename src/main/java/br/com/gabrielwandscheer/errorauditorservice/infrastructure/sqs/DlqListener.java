package br.com.gabrielwandscheer.errorauditorservice.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.gabrielwandscheer.errorauditorservice.application.ErrorAuditService;
import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class DlqListener {

    private final ErrorAuditService errorAuditService;
    private final String queueName;

    public DlqListener(ErrorAuditService errorAuditService, @Value("${queue.name}") String queueName) {
        this.errorAuditService = errorAuditService;
        this.queueName = queueName;
    }

    @SqsListener(value = "${queue.name}")
    public void receiveMessage(String payload) throws JsonProcessingException {
        errorAuditService.auditError(payload, queueName);
    }
}
