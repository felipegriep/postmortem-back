package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.UserAccountDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
import com.griep.postmortem.domain.model.UserAccount;
import com.griep.postmortem.domain.util.UserAccountMapper;
import com.griep.postmortem.infra.exception.NotFoundException;
import com.griep.postmortem.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.griep.postmortem.domain.util.UserAccountMapper.toDTO;
import static com.griep.postmortem.domain.util.UserAccountMapper.toEntity;

@Service
@RequiredArgsConstructor
public class UserAccountService implements IUserAccountService {

    private final UserAccountRepository repository;

    @Override
    public List<UserAccountResponseDTO> list() {
        return repository.findAllByOrderByNameAsc()
                .stream()
                .map(UserAccountMapper::toDTO)
                .toList();
    }

    @Override
    public Page<UserAccountResponseDTO> list(final String name,
                                             final String email,
                                             final Boolean active,
                                             final Pageable pageable) {
        return repository
                .findAll(filter(name, email, active), pageable)
                .map(UserAccountMapper::toDTO);
    }

    private Example<UserAccount> filter(final String name, final String email, final Boolean active) {
        return Example.of(UserAccount.builder()
                        .name(name)
                        .email(email)
                        .active(active)
                        .build(),
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withMatcher("name",
                                ExampleMatcher.GenericPropertyMatchers
                                        .contains())
        );
    }

    @Override
    public UserAccountResponseDTO get(final Long id) {
        return toDTO(getUserAccount(id));
    }

    @Override
    public Optional<UserAccountResponseDTO> get(String email, String externalId) {
        return repository.findByEmailIgnoreCaseOrExternalIdEquals(email, externalId)
                .map(UserAccountMapper::toDTO);
    }

    @Override
    public UserAccount getUserAccount(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Account not found!"));
    }

    @Override
    public UserAccount getUserAccount(final String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User Account %s not found!".formatted(email)));
    }

    @Override
    public UserAccountResponseDTO create(final UserAccountDTO userAccount) {
        var recorder = toEntity(new UserAccount(), userAccount);

        recorder = repository.saveAndFlush(recorder);

        return toDTO(recorder);
    }

    @Override
    public UserAccountResponseDTO update(final Long id, final UserAccountDTO userAccount) {
        var recorder = this.getUserAccount(id);

        recorder = toEntity(recorder, userAccount);

        return toDTO(repository.saveAndFlush(recorder));
    }
}
