package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.PostmortemDoc;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostmortemDocRepository extends JpaRepository<PostmortemDoc, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select max(d.version) " +
            "from PostmortemDoc d " +
            "where d.incident.id = :incidentId")
    Integer findLastVersionForUpdate(@Param("incidentId") final Long incidentId);
    List<PostmortemDoc> findByIncidentIdOrderByVersionAsc(final Long incidentId);
    Optional<PostmortemDoc> findByIncidentIdAndVersion(final Long incidentId, final Integer version);
}

