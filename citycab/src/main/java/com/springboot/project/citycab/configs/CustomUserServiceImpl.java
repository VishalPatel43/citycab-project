//package com.springboot.project.citycab.configs;
//
//import com.csitpark.realestate.dao.UserRepository;
//import com.csitpark.realestate.domain.USER_ROLE;
//import com.csitpark.realestate.entity.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CustomUserServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository; // change to the UserService
//
//    public CustomUserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
////        User user  = userRepository.findUserByEmail(username);
//        User user = userRepository.findByEmail(username);
//
////        User user = userRepository.findByPhoneNo(username);
//        if (user == null) {
////            throw new UsernameNotFoundException("User not found with email: " + username);
//            throw new UsernameNotFoundException("User not found with phoneNo: " + username);
//        }
//
//        USER_ROLE role = user.getRole();
//
//        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;
//
//        System.out.println("Role:  ---- " + role);
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add((new SimpleGrantedAuthority(role.toString())));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(), user.getPassword(), authorities);
////        return new org.springframework.security.core.userdetails.User(
////                user.getPhoneNo(), user.getPassword(), authorities);
//    }
//}
