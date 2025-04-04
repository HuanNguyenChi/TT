package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.dto.request.CreateUserRequest;
import com.vtit.project.thuctap.dto.request.ListUserRequest;
import com.vtit.project.thuctap.dto.request.UpdateUserRequest;
import com.vtit.project.thuctap.dto.response.UserDTO;
import com.vtit.project.thuctap.entity.Role;
import com.vtit.project.thuctap.entity.User;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.RoleRepository;
import com.vtit.project.thuctap.repository.UserRepository;
import com.vtit.project.thuctap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;


    @Override
    public Page<UserDTO> findUserFilter(ListUserRequest request, Pageable pageable) {
        Page<UserDTO> page = userRepository.findUserFilter(request, pageable);
        if(page.isEmpty()){
            throw new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER);
        }
        return page;
    }

    @Override
    public UserDTO findDetailUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteUserById(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }


    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = modelMapper.map(createUserRequest, User.class);

        List<Role> roles = roleRepository.findByCode("ROLE_USER")
                .map(List :: of)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.ROLE));
        user.setRoleList(roles);

        validateCreateUser(user);
        User savedUser = userRepository.save(user);

        roles.forEach(role -> role.getUserList().add(savedUser));
        roleRepository.saveAll(roles);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest  request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        validateUpdateUser(user,request);

        List<Role> newRoles = roleRepository.findAllById(request.getIdsRole());
        Set<Role> eRoles = new HashSet<>(user.getRoleList());
        eRoles.addAll(newRoles);
        user.setRoleList(new ArrayList<>(eRoles));

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }


    private void validateCreateUser(User user) {
        Stream.of(
                        new AbstractMap.SimpleEntry<>(userRepository.existsByUsername(user.getUsername()), ResponseObject.USERNAME),
                        new AbstractMap.SimpleEntry<>(userRepository.existsByEmail(user.getEmail()), ResponseObject.EMAIL),
                        new AbstractMap.SimpleEntry<>(userRepository.existsByIdentityNumber(user.getIdentityNumber()), ResponseObject.IDENTITY_NUMBER)
                )
                .filter(AbstractMap.SimpleEntry::getKey)
                .findFirst()
                .ifPresent(entry -> {
                    throw new ThucTapException(ResponseCode.EXISTED, entry.getValue());
                });
    }
    private void validateUpdateUser(User user, UpdateUserRequest request) {
        if(!user.getUsername().equals(request.getUsername())){
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.USERNAME);
            }
        }
        if(!user.getEmail().equals(request.getEmail())){
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.EMAIL);
            }
        }
        if(!user.getIdentityNumber().equals(request.getIdentityNumber())){
            if (userRepository.existsByIdentityNumber(request.getIdentityNumber())) {
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.IDENTITY_NUMBER);
            }
        }
    }
}
