package com.cg.estate_tracker.service;

import com.cg.estate_tracker.model.User;
import com.cg.estate_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail()) // Important: use email
                    .password(user.getPassword())
                    .build();
        }

        throw new UsernameNotFoundException(email);
    }
}
