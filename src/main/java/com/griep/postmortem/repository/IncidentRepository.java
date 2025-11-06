package com.griep.postmortem.repository;

import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.model.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    @Query("SELECT DISTINCT i FROM Incident i LEFT JOIN FETCH i.reporter WHERE " +
            "(:service IS NULL OR LOWER(i.service) LIKE LOWER(CONCAT('%', :service, '%'))) AND " +
            "(:severity IS NULL OR i.severity = :severity) AND " +
            "(:status IS NULL OR i.status = :status)")
    Page<Incident> findAllWithReporter(final String service,
                                       final SeverityEnum severity,
                                       final StatusEnum status,
                                       final Pageable pageable);
}
