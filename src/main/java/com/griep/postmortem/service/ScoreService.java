package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.response.ScoreDTO;
import com.griep.postmortem.domain.dto.response.ScoreDTO.Breakdown;
import com.griep.postmortem.domain.dto.response.ScoreDTO.Checks;
import com.griep.postmortem.domain.model.Incident;
import com.griep.postmortem.domain.model.RootCause;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.ActionItemRepository;
import com.griep.postmortem.repository.IncidentEventRepository;
import com.griep.postmortem.repository.IncidentRepository;
import com.griep.postmortem.repository.RootCauseRepository;
import com.griep.postmortem.service.util.ScoreResult;
import com.griep.postmortem.service.util.ScoreResult.RootCauseResult;
import com.griep.postmortem.service.util.ScoreResult.TimelineResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.griep.postmortem.Constants.*;
import static com.griep.postmortem.domain.enums.ActionTypeEnum.CORRECTIVE;
import static com.griep.postmortem.domain.enums.ActionTypeEnum.PREVENTIVE;
import static com.griep.postmortem.domain.enums.EventTypeEnum.COMMUNICATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreService implements IScoreService{

    private final IncidentRepository incidentRepository;
    private final IncidentEventRepository incidentEventRepository;
    private final RootCauseRepository rootCauseRepository;
    private final ActionItemRepository actionItemRepository;

    @Override
    public ScoreDTO compute(final Incident incident) {
        var timelineResult = computeTimelineResult(incident.getId());

        var hasImpact = isHasImpact(incident);

        var rootCauseResult = computeRootCause(incident.getId());

        var hasActionsBoth = isHasActionsBoth(incident.getId());

        var scoreResult = computeScore(timelineResult, hasImpact, rootCauseResult, hasActionsBoth);

        return generateScoreReport(
                incident.getId(),
                scoreResult,
                timelineResult,
                hasImpact,
                rootCauseResult,
                hasActionsBoth);
    }

    @Override
    public ScoreDTO compute(final Long incidentId) {
        var incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new NotFoundException("Incident not found"));
        return compute(incident);
    }

    private static ScoreDTO generateScoreReport(
            final Long incidentId,
            final ScoreResult scoreResult,
            final TimelineResult timelineResult,
            final boolean hasImpact,
            final RootCauseResult rootCauseResult,
            final boolean hasActionsBoth) {
        return new ScoreDTO(
                incidentId,
                scoreResult.total(),
                new Breakdown(
                        scoreResult.timeline(),
                        scoreResult.impact(),
                        scoreResult.whys(),
                        scoreResult.root(),
                        scoreResult.actions(),
                        scoreResult.comm()),
                new Checks(
                        timelineResult.hasMinEvents(),
                        hasImpact,
                        rootCauseResult.hasFiveWhys(),
                        rootCauseResult.hasRootAndFactors(),
                        hasActionsBoth,
                        timelineResult.hasCommunication()),
                LocalDateTime.now()
        );
    }

    private static ScoreResult computeScore(
            final TimelineResult timelineResult,
            final boolean hasImpact,
            final RootCauseResult rootCauseResult,
            final boolean hasActionsBoth) {
        // Pontuação
        var timeline = timelineResult.hasMinEvents() ? P_TIMELINE : 0;
        var impact = hasImpact ? P_IMPACT : 0;
        var whys = rootCauseResult.hasFiveWhys() ? P_WHYS : 0;
        var root = rootCauseResult.hasRootAndFactors() ? P_ROOT_FACTORS : 0;
        var actions = hasActionsBoth ? P_ACTIONS : 0;
        var comm = timelineResult.hasCommunication() ? P_COMMUNICATION : 0;

        var total = timeline + impact + whys + root + actions + comm;

        return new ScoreResult(
                timeline,
                impact,
                whys,
                root,
                actions,
                comm,
                total);
    }

    private boolean isHasActionsBoth(final Long incidentId) {
        // Ações
        var hasCorrective = actionItemRepository
                .countByIncidentIdAndActionTypeEqualsAndOwnerIsNotNullAndDueDateIsNotNull(
                        incidentId,
                        CORRECTIVE) > 0;
        var hasPreventive = actionItemRepository
                .countByIncidentIdAndActionTypeEqualsAndOwnerIsNotNullAndDueDateIsNotNull(
                        incidentId,
                        PREVENTIVE) > 0;
        return hasCorrective && hasPreventive;
    }

    private RootCauseResult computeRootCause(final Long incidentId) {
        // Root cause + 5 whys
        var rcOpt = rootCauseRepository.findByIncidentId(incidentId);
        var hasFiveWhys = rcOpt.map(this::hasFiveWhysFilled).orElse(false);
        var hasRootAndFactors = rcOpt.map(this::hasRootAndFactorsFilled).orElse(false);
        return new RootCauseResult(hasFiveWhys, hasRootAndFactors);
    }

    private boolean isHasImpact(final Incident inc) {
        // Impacto
        return hasText(inc.getImpactShort());
    }

    private TimelineResult computeTimelineResult(final Long incidentId) {
        // Timeline
        var events = incidentEventRepository.countByIncidentId(incidentId);
        var hasMinEvents = events >= 4;
        var hasCommunication = incidentEventRepository
                .existsByIncidentIdAndType(incidentId, COMMUNICATION);
        return new TimelineResult(hasMinEvents, hasCommunication);
    }

    private boolean hasText(final String text) {
        return text != null && !text.trim().isEmpty();
    }

    private boolean hasFiveWhysFilled(final RootCause rootCause) {
        return hasText(rootCause.getWhy1()) &&
                hasText(rootCause.getWhy2()) &&
                hasText(rootCause.getWhy3()) &&
                hasText(rootCause.getWhy4()) &&
                hasText(rootCause.getWhy5());
    }

    private boolean hasRootAndFactorsFilled(final RootCause rootCause) {
        return hasText(rootCause.getRootCauseText()) &&
                hasText(rootCause.getContributingFactors());
    }
}