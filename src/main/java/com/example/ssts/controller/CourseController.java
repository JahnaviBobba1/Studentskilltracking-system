package com.example.ssts.controller;

import com.example.ssts.model.Course;
import com.example.ssts.model.User;
import com.example.ssts.service.CourseService;
import com.example.ssts.service.SubscriptionService;
import com.example.ssts.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public CourseController(CourseService courseService, UserService userService, SubscriptionService subscriptionService) {
        this.courseService = courseService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    private boolean isAdmin(Principal principal) {
        if (principal == null) return false;
        User user = userService.getUserByUsername(principal.getName());
        return user.getRole().equals(User.ROLE_ADMIN);
    }

    @GetMapping("/all")
    public String getAllCourses(Model model, Principal principal) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        
        if (isAdmin(principal)) {
            Map<Long, List<User>> courseSubscribers = new HashMap<>();
            for (Course course : courses) {
                List<User> subscribers = subscriptionService.findUsersSubscribedToCourse(course.getId());
                courseSubscribers.put(course.getId(), subscribers);
            }
            model.addAttribute("courseSubscribers", courseSubscribers);
        }
        
        return "courses";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/courses/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "course-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses/all";
    }

    @PostMapping("/subscribe/course/{courseId}")
    public String subscribeToCourse(@PathVariable Long courseId,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName());
            subscriptionService.subscribeToCourse(user.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", "Course subscribed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/courses/all";
    }
}
