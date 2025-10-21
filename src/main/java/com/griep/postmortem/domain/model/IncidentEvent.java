package com.griep.postmortem.domain.model;

import com.griep.postmortem.domain.enums.EventTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "INCIDENT_EVENT", indexes = {
    @Index(name = "IDX_INCIDENT_EVENT_INCIDENT_AT", columnList = "ID_INCIDENT, EVENT_AT"),
    @Index(name = "IDX_INCIDENT_EVENT_TYPE", columnList = "TYPE"),
    @Index(name = "IDX_INCIDENT_EVENT_ACTOR", columnList = "ID_USER_ACCOUNT_ACTOR")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class IncidentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INCIDENT_EVENT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INCIDENT", nullable = false, foreignKey = @ForeignKey(name = "FK_INCIDENT_EVENT_ID_INCIDENT"))
    private Incident incident;

    @Column(name = "EVENT_AT", nullable = false)
    private LocalDateTime eventAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 13)
    private EventTypeEnum type;

    @Lob
    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_ACCOUNT_ACTOR", foreignKey = @ForeignKey(name = "FK_INCIDENT_EVENT_ID_USER_ACCOUNT_ACTOR"))
    private UserAccount actor;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
