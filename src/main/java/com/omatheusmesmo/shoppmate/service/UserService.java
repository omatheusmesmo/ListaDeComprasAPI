package com.omatheusmesmo.shoppmate.service;


import com.omatheusmesmo.shoppmate.entity.User;
import com.omatheusmesmo.shoppmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User addUser(User user){
        isUserValid(user);
        encryptPassword(user);
        userRepository.save(user);
        return user;
    }

    private void encryptPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User editUser(User user){
        findUserById(user.getId());
        validateIfDataIsNullOrEmpty(user);
        encryptPassword(user);
        userRepository.save(user);
        return user;
    }

    public void removeUser(Long id){
        findUserById(id);
        userRepository.deleteById(id);
    }

    protected void findUserById(Long id) {
        if(userRepository.findById(id).isEmpty()){
            throw new NoSuchElementException("User not found!");
        }
    }

    public void isUserValid(User user){
        validateIfDataIsNullOrEmpty(user);
        validateIfUserExists(user.getEmail());
    }

    protected void validateIfUserExists(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("E-mail is already being used!");
        }
    }

    protected void validateIfDataIsNullOrEmpty(User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()){
            throw new IllegalArgumentException("E-mail is required!");
        }

        if(user.getPassword() == null || user.getPassword().isBlank()){
            throw new IllegalArgumentException("Password is required!");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> returnAllUsers() {
        return userRepository.findAll();
    }
}
