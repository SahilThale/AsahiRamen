package com.sheryians.major.service;

import com.sheryians.major.model.Reservation;
import com.sheryians.major.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

}
