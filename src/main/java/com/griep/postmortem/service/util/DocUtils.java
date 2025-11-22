package com.griep.postmortem.service.util;

import com.griep.postmortem.domain.model.ActionItem;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.griep.postmortem.domain.enums.ActionStatusEnum.DONE;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor(access = PRIVATE)
public class DocUtils {

    public static void writeWhy(final StringBuilder builder, final int number, final String text) {
        if (hasText(text)) {
            builder.append(number).append(". ").append(safe(text)).append("\n");
        }
    }

    public static void writeActionsTable(final StringBuilder builder,
                                   final List<ActionItem> actionItems,
                                   final ZoneId zone) {
        builder.append("| Descrição | Owner | Due (local) | Status | Fechada em | SLA |\n|---|---|---|---|---|---|\n");
        if (actionItems.isEmpty()) {
            builder.append("| - | - | - | - | - | - |\n\n");
            return;
        }
        for (var action : actionItems) {
            var dueLocal = fmtLocal(action.getDueDate());
            var closedLocal = fmtLocal(action.getCompletedAt());
            var sla = slaLabel(action);
            var desc = safe(action.getDescription());
            if (hasText(action.getEvidenceLink())) {
                desc += " [" + "evidência" + "](" + action.getEvidenceLink().trim() + ")";
            }
            builder.append("| ").append(desc)
                    .append(" | ").append(safe(action.getOwner() != null ? action.getOwner().getName() : "-"))
                    .append(" | ").append(dueLocal)
                    .append(" | ").append(action.getStatus().getDescription())
                    .append(" | ").append(closedLocal != null ? closedLocal : "-")
                    .append(" | ").append(sla).append(" |\n");
        }
        builder.append("\n");
    }

    private static String slaLabel(final ActionItem actionItem) {
        if (actionItem.getStatus() == DONE) {
            if (actionItem.getDueDate() != null && actionItem.getCompletedAt() != null && !actionItem.getCompletedAt().isAfter(actionItem.getDueDate())) {
                return "**No prazo**";
            }
            return "*Fora do prazo*";
        }
        if (actionItem.getDueDate() != null && now(of("America/Sao_Paulo")).toLocalDateTime().isAfter(actionItem.getDueDate())) {
            return "*Atrasada ●*";
        }
        return "-";
    }

    public static String fmtLocal(final LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public static String fmtLocal(final Instant ts, final ZoneId zone) {
        if (ts == null) return "-";
        return ts.atZone(zone).format(ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String fmtDur(final Duration duration) {
        return duration == null ? "-" : humanize(duration);
    }

    private static String humanize(final Duration duration) {
        long m = duration.toMinutes();
        if (m < 60) return m + "m";
        long h = m / 60;
        long rm = m % 60;
        return rm == 0 ? h + "h" : h + "h" + rm + "m";
    }

    public static String safe(String string) {
        if (string == null) {
            return "-";
        }

        return string;
    }
}
