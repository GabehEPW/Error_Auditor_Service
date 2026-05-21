package br.com.gabrielwandscheer.errorauditorservice.infrastructure.sqs;

import br.com.gabrielwandscheer.errorauditorservice.application.ErrorAuditService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Component
public class DlqListener {

    private static final Logger logger = LoggerFactory.getLogger(DlqListener.class);

    private final ErrorAuditService service;
    private final String queueName;

    public DlqListener(ErrorAuditService service,
                       @Value("${app.sqs.queue-name}") String queueName) {
        this.service = service;
        this.queueName = queueName;
    }

    @SqsListener("${app.sqs.dlq-name}")
    public void onMessage(String payload, @Headers Map<String, Object> headers) {
        Object error = headers.get("error");
        String errorDetails = null;
        if (error != null) {
            logger.warn("DLQ message contains error header: {}", error);
            errorDetails = String.valueOf(error);
        }
        service.saveFromDlq(payload, queueName, errorDetails);
        logger.info("DLQ message persisted successfully");
    }
}
