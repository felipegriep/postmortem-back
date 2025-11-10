package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.IncidentDTO;
import com.griep.postmortem.domain.dto.response.IncidentResponseDTO;
import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.griep.postmortem.domain.util.IncidentMapper.toDTOWithMttaAndMttrAndScore;
import static com.griep.postmortem.domain.util.IncidentMapper.toEntity;
import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService implements IIncidentService {

    private final IncidentRepository repository;
    private final IIncidentMetricsService metricsService;
    private final IScoreService scoreService;
    private final IUserAccountService userAccountService;

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentResponseDTO> list(final String service,
                                          final SeverityEnum severity,
                                          final StatusEnum status,
                                          final Pageable pageable) {

        var page = repository.findAllWithReporter(service, severity, status, pageable);
        var content = page.getContent()
                .parallelStream()
                .map(incident -> toDTOWithMttaAndMttrAndScore(
                        incident,
                        metricsService.calculateMttaAndMttr(incident.getId(), incident.getStartedAt()),
                        scoreService.compute(incident).score()))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponseDTO get(final Long id) {
        var incident = getEntity(id);
        return toDTOWithMttaAndMttrAndScore(incident, metricsService
                        .calculateMttaAndMttr(id, incident.getStartedAt()),
                scoreService.compute(incident).score());
    }

    @Override
    public Incident getEntity(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident not found!"));
    }

    @Override
    @Transactional
    public Long create(final IncidentDTO incident, final String userEmail) {
        var userAccount = userAccountService.getUserAccount(userEmail);
        var recorder = toEntity(new Incident(), incident, userAccount);

        recorder = repository.saveAndFlush(recorder);

        return recorder.getId();
    }

    @Override
    @Transactional
    public IncidentResponseDTO update(final Long id, final IncidentDTO incident, String userEmail) {
        var recorder = this.getEntity(id);
        var userAccount = userAccountService.getUserAccount(userEmail);

        recorder = toEntity(recorder, incident, userAccount);

        recorder = repository.saveAndFlush(recorder);

        return toDTOWithMttaAndMttrAndScore(
                recorder,
                metricsService.calculateMttaAndMttr(id, recorder.getStartedAt()),
                scoreService.compute(recorder).score());
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        var incident = this.getEntity(id);

        repository.delete(incident);
    }
}
