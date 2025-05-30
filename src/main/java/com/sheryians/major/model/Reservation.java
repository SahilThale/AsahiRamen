package com.sheryians.major.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone;
    private int guests;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    private String specialRequests;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Link reservation to a user

    public Reservation() {}

    public Reservation(String fullName, String email, String phone, int guests, LocalDate date, LocalTime time, String specialRequests, User user) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.guests = guests;
        this.date = date;
        this.time = time;
        this.specialRequests = specialRequests;
        this.user = user;
    }
}
