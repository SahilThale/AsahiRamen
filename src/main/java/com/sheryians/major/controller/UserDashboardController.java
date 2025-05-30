package com.sheryians.major.controller;

import com.sheryians.major.model.Reservation;
import com.sheryians.major.model.User;
import com.sheryians.major.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserDashboardController {

    private final UserService userService;

    public UserDashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String getUserDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = (User) userService.loadUserByUsername(email);
        List<Reservation> reservations = userService.getUserReservations(email);

        model.addAttribute("user", user);
        model.addAttribute("reservations", reservations);

        return "user_dashboard"; // dashboard.html
    }
}
