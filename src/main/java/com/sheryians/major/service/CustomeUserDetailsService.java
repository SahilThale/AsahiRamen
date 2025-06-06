package com.sheryians.major.service;

import com.sheryians.major.model.CustomeUserDetail;
import com.sheryians.major.model.User;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomeUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User>user= userRepository.findUserByEmail(email);
        user.orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return user.map(CustomeUserDetail::new).get();
    }
}
