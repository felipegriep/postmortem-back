package com.griep.postmortem.domain.model;

import com.griep.postmortem.domain.enums.SeverityEnum;
import com.griep.postmortem.domain.enums.StatusEnum;
import com.griep.postmortem.domain.util.SeverityEnumConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "INCIDENT", indexes = {
    @Index(name = "IDX_INCIDENT_SERVICE", columnList = "SERVICE"),
    @Index(name = "IDX_INCIDENT_SEVERITY", columnList = "SEVERITY"),
    @Index(name = "IDX_INCIDENT_STATUS", columnList = "STATUS"),
    @Index(name = "IDX_INCIDENT_STARTED_AT", columnList = "STARTED_AT")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INCIDENT")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "SERVICE", nullable = false, length = 120)
    private String service;

    @Column(name = "SEVERITY", nullable = false, length = 5)
    private SeverityEnum severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 11)
    @Builder.Default
    private StatusEnum status = StatusEnum.OPEN;

    @Column(name = "STARTED_AT", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ENDED_AT")
    private LocalDateTime endedAt;

    @Column(name = "IMPACT_SHORT", length = 500)
    private String impactShort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_ACCOUNT_REPORTER", foreignKey = @ForeignKey(name = "FK_INCIDENT_ID_USER_ACCOUNT_REPORTER"))
    private UserAccount reporter;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IncidentEvent> events = new ArrayList<>();

    @OneToOne(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private RootCause rootCause;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ActionItem> actionItems = new ArrayList<>();

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostmortemDoc> postmortemDocs = new ArrayList<>();
}
