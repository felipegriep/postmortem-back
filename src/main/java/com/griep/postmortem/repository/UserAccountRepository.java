package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    List<UserAccount> findAllByOrderByNameAsc();
    Optional<UserAccount> findByEmailIgnoreCaseOrExternalIdEquals(final String email, final String externalId);
    Optional<UserAccount> findByEmailIgnoreCase(final String email);
}
