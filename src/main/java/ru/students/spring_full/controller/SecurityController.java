package ru.students.spring_full.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.students.spring_full.dto.UserDto;
import ru.students.spring_full.service.UserService;

@Controller
public class SecurityController {
    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDto());

        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        var existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null) {
            result.rejectValue("email", "", "Email already in use");
        }

        model.addAttribute("user", userDto);

        if (result.hasErrors()) {
            return "register";
        }

        userService.saveUser(userDto);

        return "redirect:/register?success";
    }


    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());

        return "users";
    }
}
