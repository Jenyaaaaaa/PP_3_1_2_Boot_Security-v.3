package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserDto;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;
import java.util.*;


@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
    }

    public record LoginRequest(String username, String password) {
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // возвращаем имя представления для страницы входа
    }

    @GetMapping("/user")
    public String getHome(Model model, Principal principal) {
        model.addAttribute("user", userService.loadUserByUsername(principal.getName()));
        return "user-home";
    }

    @GetMapping("/logout") // Logout с любой страницы
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/admin")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "user-list";
    }

    @GetMapping("/admin/user-create")
    public String createUserForm(Model model) {
        List<Role> listRoles = roleService.getAllRoles();
        model.addAttribute("listRoles", listRoles);
        return "user-create";
    }

    @PostMapping("/admin/user-create")
    public String createUser(@RequestParam(name = "listRoles", required = false) List<Long> listRoles, User user) {
        if (listRoles != null && !listRoles.isEmpty()) {
            List<Role> roles = roleService.findRolesByIds(listRoles);
            user.setRoles(roles);
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-update";
    }

    @PostMapping("/admin/user-update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto updatedUserDto, @RequestParam(name = "listRoles", required = false) List<Long> listRoles) {
        User existingUser = userService.findById(id);
        if (existingUser != null) {
            // Обновляем только те поля, которые указаны в UserDto
            existingUser.setFirstName(updatedUserDto.getFirstName());
            existingUser.setLastName(updatedUserDto.getLastName());
            existingUser.setUsername(updatedUserDto.getUsername());

            // Проверяем, что список идентификаторов ролей не null
            if (listRoles != null) {
                List<Role> roles = roleService.findRolesByIds(listRoles);
                existingUser.setRoles(roles);
            }

            userService.saveUser(existingUser);
            return "redirect:/admin";
        } else {
            return "redirect:/admin";
        }
    }


    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}

