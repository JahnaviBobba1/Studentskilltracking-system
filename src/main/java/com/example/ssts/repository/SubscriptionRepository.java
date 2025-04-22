package com.example.ssts.repository;

import com.example.ssts.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByUserIdAndCourseIsNotNull(Long userId);
    List<Subscription> findByUserIdAndEventIsNotNull(Long userId);
    List<Subscription> findByEventId(Long eventId);
    List<Subscription> findByCourseId(Long courseId);
    
    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.course IS NOT NULL")
    List<Subscription> findCourseSubscriptionsByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.event IS NOT NULL")
    List<Subscription> findEventSubscriptionsByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
       "FROM Subscription s WHERE s.user.id = :userId AND s.event.id = :eventId")
    boolean existsByUserIdAndEventId(@Param("userId") Long userId, 
                               @Param("eventId") Long eventId);

                               @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.user.id = :userId AND s.course.id = :courseId")
                               boolean existsByUserIdAndCourseId(@Param("userId") Long userId, 
                                                               @Param("courseId") Long courseId);

}