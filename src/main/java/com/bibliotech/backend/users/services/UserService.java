package com.bibliotech.backend.users.services;

import com.bibliotech.backend.exceptions.DataConflictException;
import com.bibliotech.backend.exceptions.InvalidOperationException;
import com.bibliotech.backend.exceptions.ResourceNotFoundException;
import com.bibliotech.backend.users.models.dtos.UserRequestDTO;
import com.bibliotech.backend.users.models.dtos.UserResponseDTO;
import com.bibliotech.backend.users.models.dtos.UserUpdateDTO;
import com.bibliotech.backend.users.models.entities.User;
import com.bibliotech.backend.users.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DataConflictException("Email already registered: " + dto.getEmail());
        }
        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new DataConflictException("CPF already registered: " + dto.getCpf());
        }

        var user = new User();
        BeanUtils.copyProperties(dto, user);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + id));

        if (Boolean.TRUE.equals(user.getDisabled())) {
            throw new InvalidOperationException("Access denied: User account is disabled.");
        }

        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateMyProfile(UserUpdateDTO dto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser.setName(dto.getName());
        currentUser.setEmail(dto.getEmail());
        currentUser.setPhone(dto.getPhone());
        currentUser.setAddress(dto.getAddress());

        User updatedUser = userRepository.save(currentUser);
        return toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found with ID: " + id));

        userRepository.delete(user);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}