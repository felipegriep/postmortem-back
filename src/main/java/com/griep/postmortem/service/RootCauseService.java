package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.RootCauseDTO;
import com.griep.postmortem.domain.dto.response.RootCauseResponseDTO;
import com.griep.postmortem.domain.model.RootCause;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.RootCauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.griep.postmortem.domain.util.RootCauseMapper.toDTO;
import static com.griep.postmortem.domain.util.RootCauseMapper.toEntity;

@Service
@RequiredArgsConstructor
public class RootCauseService implements IRootCauseService {

    private final IIncidentService incidentService;
    private final RootCauseRepository repository;

    @Override
    public void create(final Long incidentId, final RootCauseDTO rootCause) {
        var incident = incidentService.getEntity(incidentId);

        if (repository.findByIncidentId(incidentId).isPresent()) {
            throw new IllegalStateException("Root cause already exists");
        }

        var recorder = toEntity(incident, new RootCause(), rootCause);
        repository.saveAndFlush(recorder);
    }

    @Override
    public RootCauseResponseDTO update(final Long incidentId, final RootCauseDTO rootCause) {
        var incident = incidentService.getEntity(incidentId);
        var recorder = repository.findByIncidentId(incidentId)
                .orElseThrow(() -> new NotFoundException("Root cause not found"));

        recorder = toEntity(incident, recorder, rootCause);
        recorder = repository.saveAndFlush(recorder);

        return toDTO(recorder);
    }

    @Override
    public Optional<RootCauseResponseDTO> get(Long incidentId) {
        return toDTO(repository.findByIncidentId(incidentId));
    }
}
