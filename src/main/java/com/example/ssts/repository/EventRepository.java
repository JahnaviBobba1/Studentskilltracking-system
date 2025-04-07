package com.example.ssts.repository;

import com.example.ssts.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Add this custom query method
    List<Event> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}