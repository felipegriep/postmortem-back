package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.IncidentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent, Long> {

    @Query("SELECT MIN(e.eventAt) " +
            "FROM IncidentEvent e " +
            "WHERE e.incident.id = :incidentId " +
            "AND e.type = com.griep.postmortem.domain.enums.EventTypeEnum.ALERT")
    Optional<Instant> firstAlertAt(@Param("incidentId") final Long incidentId);
}
