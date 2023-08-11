package com.auction.security;

import com.auction.Entities.User;
import com.auction.exception.ResourceNotFoundException;
import com.auction.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Loading user from user database
        User user = this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Email","id"+username,0));
        return user;
    }
}
