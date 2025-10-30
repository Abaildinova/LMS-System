package kz.test.lmssystem.service.impl;

import kz.test.lmssystem.entity.Course;
import kz.test.lmssystem.exception.ResourceNotFoundException;
import kz.test.lmssystem.repository.CourseRepository;
import kz.test.lmssystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        List<Course> courses = courseRepository.findAll();
        log.info("Found {} courses", courses.size());
        return courses;
    }

    @Override
    public Course getCourseById(Long id) {
        log.info("Fetching course by id: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course with id {} not found", id);
                    return new ResourceNotFoundException("Course", id);
                });
    }

    @Override
    public Course getCourseByName(String name) {
        log.info("Fetching course by name: {}", name);
        Course course = courseRepository.findByName(name);
        if (course == null) {
            log.error("Course with name '{}' not found", name);
            throw new ResourceNotFoundException("Course", "name", name);
        }
        return course;
    }

    @Override
    public void updateCourse(Course course) {
        log.info("Updating course with id: {}", course.getId());
        if (!courseRepository.existsById(course.getId())) {
            log.error("Cannot update. Course with id {} not found", course.getId());
            throw new ResourceNotFoundException("Course", course.getId());
        }
        log.debug("Course data: {}", course);
        courseRepository.save(course);
        log.info("Course updated successfully");
    }

    @Override
    public void saveCourse(Course course) {
        log.info("Creating new course");
        log.debug("Course data: name={}, description={}", course.getName(), course.getDescription());
        courseRepository.save(course);
        log.info("Course created successfully with id: {}", course.getId());
    }

    @Override
    public void deleteCourseById(Long id) {
        log.info("Deleting course with id: {}", id);
        if (!courseRepository.existsById(id)) {
            log.error("Cannot delete. Course with id {} not found", id);
            throw new ResourceNotFoundException("Course", id);
        }
        courseRepository.deleteById(id);
        log.info("Course deleted successfully");
    }

}
