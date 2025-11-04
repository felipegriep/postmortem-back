package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.ActionItemDTO;
import com.griep.postmortem.domain.dto.response.ActionItemResponseDTO;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import com.griep.postmortem.domain.model.ActionItem;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.UserAccount;
import com.griep.postmortem.domain.util.ActionItemMapper;
import com.griep.postmortem.infra.exception.BusinessValidationException;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.ActionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static com.griep.postmortem.domain.util.ActionItemMapper.toDTO;
import static com.griep.postmortem.domain.util.ActionItemMapper.toEntity;
import static com.griep.postmortem.service.util.Constants.ZONE_ID;
import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.matchingAll;

@Service
@RequiredArgsConstructor
public class ActionItemService implements IActionItemService {
    private final ActionItemRepository repository;
    private final IIncidentService incidentService;

    @Override
    public Page<ActionItemResponseDTO> list(final Long incidentId,
                                            final ActionTypeEnum actionType,
                                            final ActionStatusEnum actionStatus,
                                            final Long ownerId,
                                            final Boolean isOverdue,
                                            final String query,
                                            final Pageable pageable) {

        var actionItems = repository.findAll(filter(incidentId, actionType, actionStatus, ownerId), pageable);

        actionItems = processFilter(isOverdue, query, pageable, actionItems);
        return actionItems.map(ActionItemMapper::toDTO);
    }

    private ActionItem getEntity(Long incidentId, Long id) {
        return repository.findAllByIncidentIdAndId(incidentId, id)
                .orElseThrow(() -> new NotFoundException("Action item not found"));
    }

    @Override
    public void create(final Long incidentId, final ActionItemDTO actionItem) {
        var incident = incidentService.getEntity(incidentId);

        validateDueDate(actionItem.dueDate());

        var recorder = toEntity(incident, new ActionItem(), actionItem);

        repository.saveAndFlush(recorder);
    }

    @Override
    public ActionItemResponseDTO update(final Long incidentId, final Long id, final ActionItemDTO actionItem) {
        var recorder = getEntity(incidentId, id);

        validateDueDate(recorder.getCreatedAt());

        recorder = repository.saveAndFlush(recorder);

        return toDTO(recorder);
    }

    private static void validateDueDate(LocalDateTime recorder) {
        if (now().isBefore(recorder)) {
            throw new BusinessValidationException("Due date must be in the future");
        }
    }

    @Override
    public void delete(final Long incidentId, final Long id) {
        var recorder = getEntity(incidentId, id);
        repository.delete(recorder);
    }

    private Page<ActionItem> processFilter(final Boolean isOverdue,
                                           final String query,
                                           final Pageable pageable,
                                           Page<ActionItem> actionItems) {
        if ((isOverdue != null && isOverdue) || (query != null && !query.isBlank())) {

            var manualFiltered = filterOverdue(isOverdue, actionItems);

            manualFiltered = applyQueryFilter(query, manualFiltered);

            actionItems = adjustPage(pageable, manualFiltered);
        }
        return actionItems;
    }

    private static Page<ActionItem> adjustPage(final Pageable pageable, final List<ActionItem> manualFiltered) {
        Page<ActionItem> actionItems;
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), manualFiltered.size());
        List<ActionItem> pageContent =
                start > manualFiltered.size() ?
                        List.of() :
                        manualFiltered.subList(start, end);

        actionItems = new PageImpl<>(pageContent, pageable, manualFiltered.size());
        return actionItems;
    }

    private List<ActionItem> applyQueryFilter(String query, List<ActionItem> manualFiltered) {
        if (query != null && !query.isBlank()) {
            manualFiltered =
                    manualFiltered.stream()
                            .filter(actionItem ->
                                    contains(actionItem.getDescription(), query.toLowerCase()) ||
                                    contains(actionItem.getEvidenceLink(), query.toLowerCase()))
                            .toList();
        }
        return manualFiltered;
    }

    private List<ActionItem> filterOverdue(Boolean isOverdue, Page<ActionItem> actionItems) {
        var manualFiltered = actionItems.getContent();
        if (isOverdue != null && isOverdue) {
            manualFiltered = actionItems.getContent().stream().filter(this::isOverdue).toList();
        }
        return manualFiltered;
    }

    private Example<ActionItem> filter(final Long incidentId,
                                       final ActionTypeEnum actionType,
                                       final ActionStatusEnum actionStatus,
                                       final Long ownerId) {
        ActionItem probe =
                ActionItem.builder()
                        .incident(Incident.builder().id(incidentId).build())
                        .actionType(actionType)
                        .status(actionStatus)
                        .build();

        if (ownerId != null) {
            probe = probe.toBuilder()
                    .owner(UserAccount.builder()
                            .id(ownerId)
                            .build())
                    .build();
        }

        return of(probe, matchingAll().withIgnoreNullValues());
    }

    private boolean contains(String s, String qLower) {
        return s != null && s.toLowerCase().contains(qLower);
    }

    private boolean isOverdue(final ActionItem actionItem) {
        if (actionItem.getDueDate() == null) {
            return false;
        }

        if (DONE.equals(actionItem.getStatus())) {
            return false;
        }

        var now = now(ZONE_ID);
        return now.isAfter(actionItem.getDueDate());
    }
}
