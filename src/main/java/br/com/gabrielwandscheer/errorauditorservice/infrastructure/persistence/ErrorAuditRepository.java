package br.com.gabrielwandscheer.errorauditorservice.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gabrielwandscheer.errorauditorservice.domain.ErrorAuditRecord;

@Repository
public interface ErrorAuditRepository extends JpaRepository<ErrorAuditRecord, UUID> {
}

