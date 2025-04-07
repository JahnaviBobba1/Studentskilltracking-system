package com.example.ssts.repository;

import com.example.ssts.model.CourseSubscription;
import com.example.ssts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Long> {
    boolean existsByUserAndCourse(User user, Course course);
    long countByUser(User user);
}