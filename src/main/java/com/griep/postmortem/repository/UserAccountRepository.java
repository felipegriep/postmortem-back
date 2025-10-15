package com.griep.postmortem.repository;

import com.griep.postmortem.domain.enums.ProviderEnum;
import com.griep.postmortem.domain.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByExternalIdAndProvider(String externalId, ProviderEnum provider);
}
