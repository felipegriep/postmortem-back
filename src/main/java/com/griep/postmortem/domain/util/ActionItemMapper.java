package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.request.ActionItemDTO;
import com.griep.postmortem.domain.dto.response.ActionItemResponseDTO;
import com.griep.postmortem.domain.model.ActionItem;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.UserAccount;
import org.modelmapper.ModelMapper;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static java.time.LocalDateTime.now;

public class ActionItemMapper {
    private final static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
    }

    public static ActionItemResponseDTO toDTO(final ActionItem actionItem) {
        return mapper.map(actionItem, ActionItemResponseDTO.class)
                .makeSla();
    }

    public static ActionItem toEntity(final Incident incident,
                                      final ActionItem actionItem,
                                      final ActionItemDTO dto,
                                      final UserAccount userAccount) {
        return actionItem.toBuilder()
                .incident(incident)
                .actionType(dto.type())
                .description(dto.description())
                .dueDate(dto.dueDate())
                .status(dto.status())
                .evidenceLink(dto.evidenceLink())
                .completedAt(DONE.equals(dto.status()) ? now() : null)
                .owner(userAccount)
                .build();
    }
}
