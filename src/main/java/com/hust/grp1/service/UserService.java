package com.hust.grp1.service;

import com.hust.grp1.dto.EntityFactory;
import com.hust.grp1.dto.user.*;
import com.hust.grp1.entity.User;
import com.hust.grp1.exception.PasswordNotMatchException;
import com.hust.grp1.exception.UserExistedException;
import com.hust.grp1.exception.UserNotFoundException;
import com.hust.grp1.repository.BookmarkRepository;
import com.hust.grp1.repository.CustomRepository;
import com.hust.grp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private static final String ROLE_USER = "ROLE_USER";

    public void createUser(UserCreateDto userCreateDto) throws UserExistedException {

        User user = EntityFactory.createUserEntity(userCreateDto, passwordEncoder);
        if(userRepository.existsByUsername(user.getUsername())) throw new UserExistedException();
        user.setRole(ROLE_USER);
        userRepository.save(user);
    }

    public User getUserFullInfo(String username) throws UserNotFoundException {

        User user = userRepository.findOneByUsername(username);
        if(user == null) throw new UserNotFoundException();
        user.setPassword(null);

        return user;
    }

    public UserInfoDto getUserInfo(int id) throws UserNotFoundException {

        UserInfoDto userInfoDto = customRepository.getUserInfo(id);
        if(userInfoDto == null) throw new UserNotFoundException();

        long bookmarkCount = bookmarkRepository.countDistinctByUser_Id(id);
        userInfoDto.setBookmarkCount(bookmarkCount);
        return userInfoDto;
    }

    public UserPageDto getUsersInfo(String searchKey, int page, CustomRepository.GetUsersSortType sortType) {

        return customRepository.getUsersInfo(searchKey, page, sortType);
    }

    public void editUser(UserCreateDto dto) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(dto.getId());
        if (!user.isPresent()) throw new UserNotFoundException();
        User newUser = user.get();
        newUser.setFirstName(dto.getFirstname());
        newUser.setLastName(dto.getLastname());
        newUser.setAbout(dto.getAbout());
        userRepository.save(newUser);
    }

    public List<UserTagDto> getAllTags(String username) {
        return customRepository.getUserTags(username);
    }

    public void changePassword(String username, ChangePasswordDto changePasswordDto) throws UserNotFoundException, PasswordNotMatchException {
        User user = userRepository.findOneByUsername(username);
        if(user == null) throw new UserNotFoundException();
        if(!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword()))
            throw new PasswordNotMatchException();

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

}
