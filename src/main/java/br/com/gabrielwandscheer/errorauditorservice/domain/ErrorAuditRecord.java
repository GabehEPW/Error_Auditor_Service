package br.com.gabrielwandscheer.errorauditorservice.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "error_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorAuditRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID errorId;

    private String queueName;

    private String payload;

    private OffsetDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ErrorStatus status;

    @Enumerated(EnumType.STRING)
    private Severity severity;
}
