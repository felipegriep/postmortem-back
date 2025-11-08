package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.response.PostmortemDocResponseDTO;

import java.util.List;

public interface IPostmortemDocService {
    Integer create(final Long incidentId);
    List<PostmortemDocResponseDTO> list(final Long incidentId);
    byte[] get(final Long incidentId, final Integer version);
}
