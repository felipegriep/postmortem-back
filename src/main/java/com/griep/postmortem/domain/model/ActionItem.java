package com.griep.postmortem.domain.model;

import com.griep.postmortem.domain.enums.ActionStatusEnum;
import com.griep.postmortem.domain.enums.ActionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACTION_ITEM", indexes = {
    @Index(name = "IDX_ACTION_ITEM_INCIDENT", columnList = "ID_INCIDENT"),
    @Index(name = "IDX_ACTION_ITEM_OWNER", columnList = "ID_USER_ACCOUNT_OWNER"),
    @Index(name = "IDX_ACTION_ITEM_STATUS", columnList = "STATUS"),
    @Index(name = "IDX_ACTION_ITEM_DUE_DATE", columnList = "DUE_DATE")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTION_ITEM")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INCIDENT", nullable = false, foreignKey = @ForeignKey(name = "FK_ACTION_ITEM_ID_INCIDENT"))
    private Incident incident;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 10)
    private ActionTypeEnum type;

    @Lob
    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_ACCOUNT_OWNER", foreignKey = @ForeignKey(name = "FK_ACTION_ITEM_ID_USER_ACCOUNT_OWNER"))
    private UserAccount owner;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 5)
    @Builder.Default
    private ActionStatusEnum status = ActionStatusEnum.TODO;

    @Column(name = "EVIDENCE_LINK", length = 500)
    private String evidenceLink;

    @Column(name = "COMPLETED_AT")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
