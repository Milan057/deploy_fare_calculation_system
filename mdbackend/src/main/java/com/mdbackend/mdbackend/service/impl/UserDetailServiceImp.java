package com.mdbackend.mdbackend.service.impl;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mdbackend.mdbackend.entities.AppUser;
import com.mdbackend.mdbackend.entities.models.CustomUserDetails;
import com.mdbackend.mdbackend.repo.BusAdminRepo;
import com.mdbackend.mdbackend.repo.BusRepo;
import com.mdbackend.mdbackend.repo.PassengerRepo;
import com.mdbackend.mdbackend.repo.SuperAdminRepo;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    private PassengerRepo passengerRepository;

    @Autowired
    private BusAdminRepo busAdminRepository;

    @Autowired
    private SuperAdminRepo superAdminRepository;

    @Autowired
    private BusRepo busRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser user = findUserByPhoneNumber(userName);
        if (user != null) {
            if (user.getDelFlg()) {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toSet());
            return new CustomUserDetails(user.getUserName(), user.getPassword(), user.getUserActive(), authorities);
        }
        throw new UsernameNotFoundException("User not found with username: " + userName);
    }

    private AppUser findUserByPhoneNumber(String userName) {
        AppUser user = passengerRepository.findByPhoneNumber(userName);
        if (user == null) {
            user = busAdminRepository.findByEmail(userName);
        }
        if (user == null) {
            user = superAdminRepository.findByEmail(userName);
        }
        if (user == null) {
            user = busRepository.findByUserName(userName);
        }
        return user;
    }
}
