package com.bibliotech.backend.users.services;

import com.bibliotech.backend.users.models.dtos.UserRequestDTO;
import com.bibliotech.backend.users.models.dtos.UserResponseDTO;
import com.bibliotech.backend.users.models.dtos.UserUpdateDTO;
import com.bibliotech.backend.users.models.entities.User;
import com.bibliotech.backend.users.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("CPF ou E-mail já cadastrado");
        }
        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF ou E-mail já cadastrado");
        }

        var user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setAdmin(false);
        user.setDisabled(false);

        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não existente"));

        if (Boolean.TRUE.equals(user.getDisabled())) {
            throw new IllegalStateException("Acesso bloqueado: Usuário inativo.");
        }

        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não existente"));

        if (Boolean.TRUE.equals(user.getAdmin())) {
            throw new IllegalStateException("O administrador do sistema não pode executar essa tarefa.");
        }

        BeanUtils.copyProperties(dto, user);

        User updatedUser = userRepository.save(user);
        return toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não existente"));

        userRepository.delete(user);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}