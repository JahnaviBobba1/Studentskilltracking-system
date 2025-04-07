package com.example.ssts.repository;

import com.example.ssts.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c JOIN c.subscriptions s WHERE s.user.username = :username")
    List<Course> findBySubscriptionsUser(@Param("username") String username);
}