package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.ActionItemDTO;
import com.griep.postmortem.domain.dto.response.ActionItemResponseDTO;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IActionItemService {
    Page<ActionItemResponseDTO> list(final Long incidentId,
                                     final ActionTypeEnum actionType,
                                     final ActionStatusEnum actionStatus,
                                     final Long ownerId,
                                     final Boolean isOverdue,
                                     final String query,
                                     final Pageable pageable);
    void create(final Long incidentId, final ActionItemDTO actionItem, final String userEmail);
    ActionItemResponseDTO update(final Long incidentId, final Long id, final ActionItemDTO actionItem, final String userEmail);
    void delete(final Long incidentId, final Long id);
}
