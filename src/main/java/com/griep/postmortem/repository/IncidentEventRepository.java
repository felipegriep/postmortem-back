package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.IncidentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent, Long> {
}
