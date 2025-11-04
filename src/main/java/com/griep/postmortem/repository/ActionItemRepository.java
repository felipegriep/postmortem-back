package com.griep.postmortem.repository;

import com.griep.postmortem.domain.enums.ActionTypeEnum;
import com.griep.postmortem.domain.model.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    Optional<ActionItem> findAllByIncidentIdAndId(final Long incidentId, final Long id);
    long countByIncidentIdAndActionTypeEqualsAndOwnerIsNotNullAndDueDateIsNotNull(
            final Long incidentId,
            final ActionTypeEnum actionType);
}
