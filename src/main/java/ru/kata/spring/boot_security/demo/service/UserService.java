package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

@Service
public interface UserService {


    public User findById (Long id);
    public List<User> findAll();
    public void saveUser (User user);
    public void deleteById (Long id);
    public User findByUsername(String username);
    public UserDetails loadUserByUsername(String username);

    public User get(Long id);
    public void updateUser(User user);



    public Collection<? extends GrantedAuthority> roles(Collection<Role> roles);

}
