package com.example.ssts.repository;

import com.example.ssts.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e FROM Event e JOIN e.subscriptions s WHERE s.user.username = :username")
    List<Event> findBySubscriptionsUser(@Param("username") String username);
}