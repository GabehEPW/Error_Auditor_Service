package br.com.gabrielwandscheer.errorauditorservice.infrastructure.persistence;

import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorAuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorAuditRepository extends JpaRepository<ErrorAuditRecord, String> {
}
