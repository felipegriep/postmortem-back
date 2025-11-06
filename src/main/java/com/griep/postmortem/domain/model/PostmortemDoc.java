package com.griep.postmortem.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "POSTMORTEM_DOC", indexes = {
    @Index(name = "IDX_POSTMORTEM_DOC_INCIDENT", columnList = "ID_INCIDENT"),
    @Index(name = "IDX_POSTMORTEM_DOC_GENERATED", columnList = "GENERATED_AT")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostmortemDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_POSTMORTEM_DOC")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INCIDENT", nullable = false, foreignKey = @ForeignKey(name = "FK_POSTMORTEM_DOC_ID_INCIDENT"))
    private Incident incident;

    @Lob
    @Column(name = "MD_CONTENT", nullable = false, columnDefinition = "LONGTEXT")
    private String mdContent;

    @Column(name = "GENERATED_AT", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "COMPLETENESS_SCORE", nullable = false)
    private Integer completenessScore;

    @Column(name = "VERSION", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
