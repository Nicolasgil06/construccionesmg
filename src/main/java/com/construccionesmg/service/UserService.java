package com.construccionesmg.service;

import com.construccionesmg.model.User;
import com.construccionesmg.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActivo(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAllClients() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ROLE_CLIENT)
                .toList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveClient(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe: " + user.getUsername());
        }
        user.setRole(User.Role.ROLE_CLIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivo(true);
        return userRepository.save(user);
    }

    public User saveAdmin(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe: " + user.getUsername());
        }
        user.setRole(User.Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivo(true);
        return userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
