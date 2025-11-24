package com.griep.postmortem.domain.model;

import com.griep.postmortem.domain.enums.ProviderEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACCOUNT", indexes = {
    @Index(name = "IDX_USER_ACCOUNT_NAME", columnList = "NAME")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER_ACCOUNT")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROVIDER", nullable = false, length = 10)
    @Builder.Default
    private ProviderEnum provider = ProviderEnum.LOCAL;

    @Column(name = "EXTERNAL_ID", length = 190)
    private String externalId;

    @Column(name = "EMAIL", nullable = false, length = 320, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "PICTURE_URL", length = 500)
    private String pictureUrl;

    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
