package com.example.ssts.service;

import com.example.ssts.model.Event;
import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import com.example.ssts.repository.EventRepository;
import com.example.ssts.repository.SubscriptionRepository;
import com.example.ssts.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, 
                      SubscriptionRepository subscriptionRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsBetween(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByDateBetween(startDate, endDate);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public void subscribeToEvent(String username, Long eventId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = getEventById(eventId);
        
        // Check if already subscribed
        if (subscriptionRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("Already subscribed to this event");
        }
        
        Subscription subscription = new Subscription(user, event);
        subscriptionRepository.save(subscription);
    }

    public List<Event> getUserSubscribedEvents(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return eventRepository.findBySubscriptionsUser(user);
    }

    public long getUserEventCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return subscriptionRepository.countByUser(user);
    }
}