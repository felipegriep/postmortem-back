package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.IncidentDTO;
import com.griep.postmortem.domain.dto.response.IncidentResponseDTO;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.util.IncidentMapper;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.griep.postmortem.domain.util.IncidentMapper.toDTO;
import static com.griep.postmortem.domain.util.IncidentMapper.toEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService implements IIncidentService {

    private final IncidentRepository repository;

    @Override
    public Page<IncidentResponseDTO> list(final String service,
                                          final SeverityEnum severity,
                                          final StatusEnum status,
                                          final Pageable pageable) {

        return repository.findAll(filter(service, severity, status), pageable).map(IncidentMapper::toDTO);
    }

    private Example<Incident> filter(final String service, final SeverityEnum severity, final StatusEnum status) {
        return Example.of(Incident.builder()
                        .service(service)
                        .severity(severity)
                        .status(status)
                        .build(),
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withMatcher("service",
                                ExampleMatcher.GenericPropertyMatchers
                                        .contains())
        );
    }

    @Override
    public IncidentResponseDTO get(final Long id) {
        return IncidentMapper
                .toDTO(getIncident(id));
    }

    private Incident getIncident(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident not found!"));
    }

    @Override
    public Long create(final IncidentDTO incident) {
        var recorder = toEntity(new Incident(), incident);

        recorder = repository.saveAndFlush(recorder);

        return recorder.getId();
    }

    @Override
    public IncidentResponseDTO update(final Long id, final IncidentDTO incident) {
        var recorder = this.getIncident(id);

        recorder = toEntity(recorder, incident);

        return toDTO(repository.saveAndFlush(recorder));
    }

    @Override
    public void delete(final Long id) {
        var incident = this.getIncident(id);

        repository.delete(incident);
    }
}
