package com.example.ssts.controller;

import com.example.ssts.model.Course;
import com.example.ssts.model.User;
import com.example.ssts.service.CourseService;
import com.example.ssts.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllCourses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Course> courses = courseService.getAllCourses();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        model.addAttribute("courses", courses);
        model.addAttribute("isAdmin", isAdmin);
        return "courses";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("success", "Course created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create course: " + e.getMessage());
        }
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "course-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/courses";
    }

    @PostMapping("/subscribe/{courseId}")
    public String subscribeToCourse(@PathVariable Long courseId, 
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            String username = userDetails.getUsername();
            courseService.subscribeToCourse(username, courseId);
            redirectAttributes.addFlashAttribute("success", "Subscribed to course successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to subscribe: " + e.getMessage());
        }
        return "redirect:/courses";
    }
}