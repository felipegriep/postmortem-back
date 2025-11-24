package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.RootCause;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RootCauseRepository extends JpaRepository<RootCause, Long> {
    Optional<RootCause> findByIncidentId(final Long incidentId);
}
