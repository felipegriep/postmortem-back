package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
}
