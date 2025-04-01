package com.example.ssts.service;

import com.example.ssts.model.Event;
import com.example.ssts.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create a new event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Retrieve all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Delete an event by ID
    public String deleteEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            eventRepository.deleteById(eventId);
            return "Event deleted successfully";
        }
        return "Event not found";
    }
}
