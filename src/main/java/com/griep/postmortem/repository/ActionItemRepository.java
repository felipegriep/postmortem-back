package com.griep.postmortem.repository;

import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import com.griep.postmortem.domain.model.ActionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    @Query("SELECT ai FROM ActionItem ai " +
            "WHERE ai.incident.id = :incidentId " +
            "AND (:actionType IS NULL " +
            "OR ai.actionType = :actionType) " +
            "AND (:actionStatus IS NULL " +
            "OR ai.status = :actionStatus) " +
            "AND (:ownerId IS NULL " +
            "OR ai.owner.id = :ownerId)")
    Page<ActionItem> findAllByFilters(@Param("incidentId") Long incidentId,
                                      @Param("actionType") ActionTypeEnum actionType,
                                      @Param("actionStatus") ActionStatusEnum actionStatus,
                                      @Param("ownerId") Long ownerId,
                                      final Pageable pageable);
    Optional<ActionItem> findAllByIncidentIdAndId(final Long incidentId, final Long id);
    long countByIncidentIdAndActionTypeEqualsAndOwnerIsNotNullAndDueDateIsNotNull(
            final Long incidentId,
            final ActionTypeEnum actionType);
}
