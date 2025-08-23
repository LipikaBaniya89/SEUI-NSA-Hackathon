package com.example.disasterapi.Service;


import com.example.disasterapi.Model.Users;
import com.example.disasterapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }
}
