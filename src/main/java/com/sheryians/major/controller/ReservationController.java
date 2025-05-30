package com.sheryians.major.controller;

import com.sheryians.major.model.Reservation;
import com.sheryians.major.model.User;
import com.sheryians.major.service.ReservationService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @GetMapping("/reservation")
    public String showReservationForm() {
        return "reservation";
    }

    @PostMapping("/reservation/submit")
    public ResponseEntity<String> submitReservation(@Valid @ModelAttribute Reservation reservation) {
        // Get logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Spring Security stores the email as the identifier

        User user = userService.findByEmail(email); // Find user by email, not first name
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        reservation.setUser(user); // Associate reservation with the user
        reservationService.saveReservation(reservation);

        return ResponseEntity.ok("Reservation successful!");
    }

    @GetMapping("/admin/reservations")
    public String showReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations";
    }

    @DeleteMapping("/reservation/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully!");
    }
}
