package com.example.ssts.service;

import com.example.ssts.model.Course;
import com.example.ssts.model.Event;
import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import com.example.ssts.repository.SubscriptionRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final EventService eventService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                             UserService userService,
                             CourseService courseService,
                             EventService eventService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.eventService = eventService;
    }

    @Transactional
    public Subscription subscribeToCourse(Long userId, Long courseId) {
        // Check for existing subscription
        if (subscriptionRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new IllegalStateException("You are already subscribed to this course");
        }
        
        User user = userService.getUserById(userId);
        Course course = courseService.getCourseById(courseId);
        
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCourse(course);
        subscription.setEvent(null); // Explicitly set event to null
        
        return subscriptionRepository.save(subscription);
    }

    public Subscription subscribeToEvent(Long userId, Long eventId) {
        // Check if subscription already exists
        if (subscriptionRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("Already subscribed to this event");
        }
        
        Subscription subscription = new Subscription();
        subscription.setUser(userService.getUserById(userId));
        subscription.setEvent(eventService.getEventById(eventId));
        //subscription.setCourse(null);
        
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> findCourseSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findByUserIdAndCourseIsNotNull(userId)
                .stream()
                .filter(sub -> sub.getCourse() != null)
                .collect(Collectors.toList());
    }

    public List<Subscription> findEventSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findByUserIdAndEventIsNotNull(userId)
                .stream()
                .filter(sub -> sub.getEvent() != null)
                .collect(Collectors.toList());
    }

    public List<User> findUsersSubscribedToEvent(Long eventId) {
        return subscriptionRepository.findByEventId(eventId)
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toList());
    }
    
    public List<User> findUsersSubscribedToCourse(Long courseId) {
        return subscriptionRepository.findByCourseId(courseId)
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toList());
    }
}
