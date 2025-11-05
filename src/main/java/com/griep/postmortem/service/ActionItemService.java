package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.ActionItemDTO;
import com.griep.postmortem.domain.dto.response.ActionItemResponseDTO;
import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import com.griep.postmortem.domain.model.ActionItem;
import com.griep.postmortem.domain.model.UserAccount;
import com.griep.postmortem.domain.util.ActionItemMapper;
import com.griep.postmortem.infra.exception.BusinessValidationException;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.ActionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static com.griep.postmortem.domain.util.ActionItemMapper.toDTO;
import static com.griep.postmortem.domain.util.ActionItemMapper.toEntity;
import static com.griep.postmortem.service.util.Constants.ZONE_ID;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ActionItemService implements IActionItemService {
    private final ActionItemRepository repository;
    private final IIncidentService incidentService;
    private final IUserAccountService userAccountService;

    @Override
    @Transactional(readOnly = true)
    public Page<ActionItemResponseDTO> list(final Long incidentId,
                                            final ActionTypeEnum actionType,
                                            final ActionStatusEnum actionStatus,
                                            final Long ownerId,
                                            final Boolean isOverdue,
                                            final String query,
                                            final Pageable pageable) {

        var actionItems = repository.findAllByFilters(incidentId, actionType, actionStatus, ownerId, pageable);

        actionItems = processFilter(isOverdue, query, pageable, actionItems);
        return actionItems.map(ActionItemMapper::toDTO);
    }

    private ActionItem getEntity(final Long incidentId, final Long id) {
        return repository.findAllByIncidentIdAndId(incidentId, id)
                .orElseThrow(() -> new NotFoundException("Action item not found"));
    }

    @Override
    @Transactional
    public void create(final Long incidentId, final ActionItemDTO actionItem, final String userEmail) {
        var incident = incidentService.getEntity(incidentId);

        UserAccount userAccount = null;
        if (actionItem.ownerId() != null && actionItem.ownerId() != 0) {
            userAccount = userAccountService.getUserAccount(actionItem.ownerId());
        }

        validateDueDate(actionItem.dueDate());

        var recorder = toEntity(incident, new ActionItem(), actionItem, userAccount);

        repository.saveAndFlush(recorder);
    }

    @Override
    @Transactional
    public ActionItemResponseDTO update(final Long incidentId,
                                        final Long id,
                                        final ActionItemDTO actionItem,
                                        final String userEmail) {
        var incident = incidentService.getEntity(incidentId);
        var recorder = getEntity(incidentId, id);
        var userAccount = userAccountService.getUserAccount(userEmail);

        recorder = toEntity(incident, recorder, actionItem, userAccount);

        validateDueDate(recorder.getDueDate());

        recorder = repository.saveAndFlush(recorder);

        return toDTO(recorder);
    }

    private static void validateDueDate(final LocalDateTime dueDate) {
        if (now().isAfter(dueDate)) {
            throw new BusinessValidationException("Due date must be in the future");
        }
    }

    @Override
    @Transactional
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

    private List<ActionItem> applyQueryFilter(final String query, Collection<ActionItem> manualFiltered) {
        if (query != null && !query.isBlank()) {
            manualFiltered =
                    manualFiltered.stream()
                            .filter(actionItem ->
                                    contains(actionItem.getDescription(), query.toLowerCase()) ||
                                    contains(actionItem.getEvidenceLink(), query.toLowerCase()))
                            .toList();
        }
        return manualFiltered.stream()
                .toList();
    }

    private List<ActionItem> filterOverdue(Boolean isOverdue, Page<ActionItem> actionItems) {
        var manualFiltered = actionItems.getContent();
        if (isOverdue != null && isOverdue) {
            manualFiltered =
                    actionItems.getContent()
                            .stream()
                            .filter(this::isOverdue)
                            .toList();
        }
        return manualFiltered;
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
