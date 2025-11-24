package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.IncidentEventDTO;
import com.griep.postmortem.domain.dto.response.IncidentEventResponseDTO;
import com.griep.postmortem.domain.model.IncidentEvent;
import com.griep.postmortem.domain.util.IncidentEventMapper;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.IncidentEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.griep.postmortem.domain.util.IncidentEventMapper.toDTO;
import static com.griep.postmortem.domain.util.IncidentEventMapper.toEntity;

@Service
@RequiredArgsConstructor
public class IncidentEventService implements IIncidentEventService {
    private final IncidentEventRepository repository;
    private final IIncidentService incidentService;
    private final IUserAccountService userAccountService;

    @Override
    @Transactional(readOnly = true)
    public List<IncidentEventResponseDTO> list(final Long incidentId) {
        return repository.findByIncidentIdOrderByEventAtAsc(incidentId)
                .stream()
                .map(IncidentEventMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public void create(final Long incidentId, final IncidentEventDTO incidentEvent, final String userEmail) {
        var incident = incidentService.getEntity(incidentId);
        var userAccount = userAccountService.getUserAccount(userEmail);

        var recorder = toEntity(incident, new IncidentEvent(), incidentEvent, userAccount);

        repository.saveAndFlush(recorder);
    }

    @Override
    @Transactional
    public IncidentEventResponseDTO update(final Long incidentId,
                                           final Long id,
                                           final IncidentEventDTO incidentEvent,
                                           final String userEmail) {
        var incident = incidentService.getEntity(incidentId);
        var recorder = this.get(id, incidentId);
        var userAccount = userAccountService.getUserAccount(userEmail);

        recorder = toEntity(incident, recorder, incidentEvent, userAccount);

        recorder = repository.saveAndFlush(recorder);

        return toDTO(recorder);
    }

    private IncidentEvent get(final Long id, final Long incidentId) {
        return repository.findByIdAndIncidentId(id, incidentId)
                .orElseThrow(() -> new NotFoundException("Event of Incident not found!"));
    }

    @Override
    @Transactional
    public void delete(final Long incidentId, final Long id) {
        var recorder = this.get(id, incidentId);

        repository.delete(recorder);
    }
}
