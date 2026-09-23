package com.ecom.service;

import com.ecom.dto.UserDto;
import com.ecom.model.User;
import com.ecom.request.CreateUserRequest;
import com.ecom.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long id);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long UserId);
    void deleteUser(Long UserId);
    UserDto convertToUserDto(User user);
}
