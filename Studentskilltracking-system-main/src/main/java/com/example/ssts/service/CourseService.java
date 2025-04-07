package com.example.ssts.service;

import com.example.ssts.model.Course;
import com.example.ssts.model.CourseSubscription;
import com.example.ssts.model.User;
import com.example.ssts.repository.CourseRepository;
import com.example.ssts.repository.CourseSubscriptionRepository;
import com.example.ssts.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseSubscriptionRepository courseSubscriptionRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository,
                       CourseSubscriptionRepository courseSubscriptionRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseSubscriptionRepository = courseSubscriptionRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void subscribeToCourse(String username, Long courseId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = getCourseById(courseId);
        
        // Check if already subscribed
        if (courseSubscriptionRepository.existsByUserAndCourse(user, course)) {
            throw new RuntimeException("Already subscribed to this course");
        }
        
        CourseSubscription subscription = new CourseSubscription(user, course);
        courseSubscriptionRepository.save(subscription);
    }

    public List<Course> getUserSubscribedCourses(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return courseRepository.findBySubscriptionsUser(user);
    }

    public long getUserCourseCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return courseSubscriptionRepository.countByUser(user);
    }
}