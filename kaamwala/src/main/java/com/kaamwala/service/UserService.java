package com.kaamwala.service;

import com.kaamwala.dtos.PageableResponse;
import com.kaamwala.dtos.UserDto;
import com.kaamwala.model.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    // Create
    UserDto createUser(UserDto userDto);

    // read //

    UserDto getUserById(String userId);

    // Get All user With pagination

    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get User by Email

    UserDto getUserByEmail(String email);

    // Search by keyword

    List<UserDto> getUSerByKeyword(String keyword);

    Optional<User> findUserByEmailOptional(String email);

    UserDto updateUser(UserDto userDto , String userId);

    void deleteUser(String userId);


    // new
    // Password  change
//    void changePassword(String email, String newPassword);
//
//    //
//    List<UserDto> getUsersByRole(String roleName);



}
