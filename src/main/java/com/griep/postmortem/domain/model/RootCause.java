package com.griep.postmortem.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ROOT_CAUSE", indexes = {
    @Index(name = "IDX_ROOT_CAUSE_INCIDENT", columnList = "ID_INCIDENT")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RootCause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROOT_CAUSE")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INCIDENT", nullable = false, unique = true, 
                foreignKey = @ForeignKey(name = "FK_ROOT_CAUSE_ID_INCIDENT"))
    private Incident incident;

    @Column(name = "WHY1", nullable = false, length = 500)
    private String why1;

    @Column(name = "WHY2", nullable = false, length = 500)
    private String why2;

    @Column(name = "WHY3", nullable = false, length = 500)
    private String why3;

    @Column(name = "WHY4", nullable = false, length = 500)
    private String why4;

    @Column(name = "WHY5", nullable = false, length = 500)
    private String why5;

    @Column(name = "ROOT_CAUSE_TEXT", nullable = false, length = 800)
    private String rootCauseText;

    @Column(name = "CONTRIBUTING_FACTORS", length = 800)
    private String contributingFactors;

    @Lob
    @Column(name = "LESSONS_LEARNED", columnDefinition = "TEXT")
    private String lessonsLearned;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
