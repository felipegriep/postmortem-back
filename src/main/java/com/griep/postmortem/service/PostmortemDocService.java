package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.response.PostmortemDocResponseDTO;
import com.griep.postmortem.domain.dto.response.ScoreDTO;
import com.griep.postmortem.domain.model.*;
import com.griep.postmortem.domain.util.PostmortemDocMapper;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.ActionItemRepository;
import com.griep.postmortem.repository.IncidentEventRepository;
import com.griep.postmortem.repository.PostmortemDocRepository;
import com.griep.postmortem.repository.RootCauseRepository;
import com.griep.postmortem.service.util.Metrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.griep.postmortem.domain.enums.ActionTypeEnum.CORRECTIVE;
import static com.griep.postmortem.domain.enums.ActionTypeEnum.PREVENTIVE;
import static com.griep.postmortem.domain.enums.EventTypeEnum.*;
import static com.griep.postmortem.domain.util.PostmortemDocMapper.toDTO;
import static com.griep.postmortem.service.util.DocUtils.*;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.of;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional
public class PostmortemDocService implements IPostmortemDocService {

    private final PostmortemDocRepository repository;
    private final IIncidentService incidentService;
    private final IIncidentMetricsService metricsService;
    private final IncidentEventRepository incidentEventRepository;
    private final RootCauseRepository rootCauseRepository;
    private final ActionItemRepository actionItemRepository;
    private final IScoreService scoreService;

    @Override
    public Integer create(final Long incidentId) {
        var incident = incidentService.getEntity(incidentId);
        var score = scoreService.compute(incident);
        var version = generateVersion(incidentId);

        var content = generate(incident, version, score);

        save(incident, version, content, score.score());

        return version;
    }

    @Override
    public List<PostmortemDocResponseDTO> list(final Long incidentId) {
        return repository.findByIncidentIdOrderByVersionAsc(incidentId)
                .stream()
                .map(PostmortemDocMapper::toDTO)
                .toList();
    }

    public byte[] get(final Long incidentId, final Integer version) {
        var doc = toDTO(repository.findByIncidentIdAndVersion(incidentId, version)
                .orElseThrow(() -> new NotFoundException("Postmortem DOC not found!")));
        return doc.getMdContent();
    }

    private Integer generateVersion(final Long incidentId) {
        var version = repository.findLastVersionForUpdate(incidentId);
        if (version == null) {
            version = 1;
        } else {
            version++;
        }
        return version;
    }

    private void save(final Incident incident,
                      final Integer version,
                      final String content,
                      final Integer completenessScore) {
        var postmortemDoc = PostmortemDoc.builder()
                .incident(incident)
                .version(version)
                .mdContent(content)
                .completenessScore(completenessScore)
                .generatedAt(now())
                .build();

        repository.saveAndFlush(postmortemDoc);
    }

    private String generate(final Incident incident,
                            final Integer version,
                            final ScoreDTO score) {
        var incidentEvents = incidentEventRepository.findByIncidentIdOrderByEventAtAsc(incident.getId());
        var rootCause = rootCauseRepository.findByIncidentId(incident.getId()).orElse(null);
        var actionItems = actionItemRepository.findByIncidentIdOrderByDueDateAscIdAsc(incident.getId());

        var metrics = computeMetrics(incident, incidentEvents);

        return renderMarkdown(incident, incidentEvents, rootCause, actionItems, score, metrics, version);
    }

    private Metrics computeMetrics(final Incident incident, final List<IncidentEvent> incidentEvents) {
        Duration[] mttaAndMttr = metricsService.calculateMttaAndMttr(incident.getId(), incident.getStartedAt());

        Duration mtta = mttaAndMttr[0];
        Duration mttr = mttaAndMttr[1];
        Duration duration = (incident.getStartedAt() != null && incident.getEndedAt() != null)
                ? between(incident.getStartedAt(), incident.getEndedAt())
                : null;

        boolean hasComm = incidentEvents.stream()
                .anyMatch(incidentEvent -> incidentEvent.getType() == COMMUNICATION);
        return new Metrics(mtta, mttr, duration, hasComm, incidentEvents.size());
    }

    private String renderMarkdown(final Incident incident,
                                  final List<IncidentEvent> incidentEvents,
                                  final RootCause rootCause,
                                  final List<ActionItem> actionItems,
                                  final ScoreDTO score,
                                  final Metrics metrics,
                                  final Integer version) {
        var zone = of("America/Sao_Paulo");

        var builder = new StringBuilder();
        // --- Cabeçalho ---
        builder.append("# Postmortem - Incidente #")
                .append(incident.getId())
                .append(" - Versão ").append(version)
                .append(": ")
                .append(safe(incident.getTitle()))
                .append("\n\n");
        builder.append("**Severidade:** ")
                .append(incident.getSeverity().getDescription())
                .append(" - **Status:** ")
                .append(incident.getStatus().getDescription())
                .append("  \n");
        builder.append("**Início:** ")
                .append(fmtLocal(incident.getStartedAt()))
                .append(" - **Fim:** ")
                .append(fmtLocal(incident.getEndedAt()))
                .append(" - **Duração:** ")
                .append(fmtDur(metrics.duration()))
                .append("\n");
        if (hasText(incident.getImpactShort())) {
            builder.append("**Impacto (resumo):** ")
                    .append(safe(incident.getImpactShort()))
                    .append("\n\n");
        }

        // --- Métricas + Score ---
        builder.append("## Métricas\n");
        builder.append("- **MTTA:** ")
                .append(fmtDur(metrics.mtta()))
                .append(" - **MTTR:** ")
                .append(fmtDur(metrics.mttr()))
                .append("\n");
        builder.append("- **Eventos:** ")
                .append(metrics.eventCount())
                .append(" - **Houve comunicação?** ")
                .append(score.checks().hasCommunication() ? "✅" : "❌")
                .append("\n");
        builder.append("- **Score:** ")
                .append(score.score())
                .append("/100  \n");
        var breakdown = score.breakdown();
        builder.append("  - Timeline: ").append(breakdown.timeline()).append("\n")
                .append("  - Impacto: ").append(breakdown.impact()).append("\n")
                .append("  - 5 Porquês: ").append(breakdown.whys()).append("\n")
                .append("  - Causa + Fatores: ").append(breakdown.rootAndFactors()).append("\n")
                .append("  - Ações: ").append(breakdown.actions()).append("\n")
                .append("  - Comunicação: ").append(breakdown.communication())
                .append("\n");
        var checks = score.checks();
        builder.append("  - Checks: ").append("\n")
                .append("   - ")
                .append(checks.hasMinEvents() ?
                        "✅ Min. eventos" :
                        "❌ Min. eventos").append("\n")
                .append("   - ").append(checks.hasImpact() ?
                        "✅ Impacto" :
                        "❌ Impacto").append("\n")
                .append("   - ").append(checks.hasFiveWhys() ?
                        "✅ 5 Porquês" :
                        "❌ 5 Porquês").append("\n")
                .append("   - ").append(checks.hasRootAndFactors() ?
                        "✅ Causa + Fatores" :
                        "❌ Causa + Fatores").append("\n")
                .append("   - ").append(checks.hasCorrectiveAndPreventiveWithOwnerAndDue() ?
                        "✅ Ações (corretiva + preventiva)" :
                        "❌ Ações (corretiva + preventiva)").append("\n")
                .append("   - ").append(checks.hasCommunication() ?
                        "✅ Comunicação" :
                        "❌ Comunicação")
                .append("\n\n");

        // --- Timeline ---
        builder.append("## Timeline\n");
        builder.append("| Quando | Tipo | Ator | Descrição |\n|---|---|---|---|\n");
        if (incidentEvents.isEmpty()) {
            builder.append("| — | — | — | Sem eventos registrados |\n\n");
        } else {
            incidentEvents.forEach(incidentEvent -> builder.append("| ")
                    .append(fmtLocal(incidentEvent.getEventAt())).append(" | ")
                    .append(incidentEvent.getType().getDescription()).append(" | ")
                    .append(safe(incidentEvent.getActor().getName())).append(" | ")
                    .append(safe(incidentEvent.getDescription())).append(" |\n"));
            builder.append("\n");
        }

        // --- Análise ---
        builder.append("## Análise de Causa\n");
        if (rootCause != null) {
            builder.append("**5 Porquês**\n");
            writeWhy(builder, 1, rootCause.getWhy1());
            writeWhy(builder, 2, rootCause.getWhy2());
            writeWhy(builder, 3, rootCause.getWhy3());
            writeWhy(builder, 4, rootCause.getWhy4());
            writeWhy(builder, 5, rootCause.getWhy5());
            builder.append("\n");
            if (hasText(rootCause.getRootCauseText())) {
                builder.append("**Causa raiz:** ")
                        .append(safe(rootCause.getRootCauseText()))
                        .append("\n\n");
            }
            if (hasText(rootCause.getContributingFactors())) {
                builder.append("**Fatores contribuintes:** ")
                        .append(safe(rootCause.getContributingFactors()))
                        .append("\n\n");
            }
            if (hasText(rootCause.getLessonsLearned())) {
                builder.append("**Lições aprendidas:** ")
                        .append(safe(rootCause.getLessonsLearned()))
                        .append("\n\n");
            }
            builder.append("\n");
        } else {
            builder.append("_Sem análise registrada._\n\n");
        }

        // --- Ações ---
        var correctives = actionItems.stream()
                .filter(a -> a.getActionType() == CORRECTIVE).toList();
        var preventives = actionItems.stream()
                .filter(a -> a.getActionType() == PREVENTIVE).toList();
        builder.append("## Ações\n");
        builder.append("### Corretivas\n");
        writeActionsTable(builder, correctives, zone);
        builder.append("### Preventivas\n");
        writeActionsTable(builder, preventives, zone);

        builder.append("\n\n");
        builder.append("---\n");
        builder.append("Documento gerado em ").append(fmtLocal(now()));


        return builder.toString();
    }
}