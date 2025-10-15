package com.griep.postmortem.repository;

import com.griep.postmortem.domain.model.PostmortemDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostmortemDocRepository extends JpaRepository<PostmortemDoc, Long> {
}

