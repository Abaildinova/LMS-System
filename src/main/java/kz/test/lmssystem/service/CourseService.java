package kz.test.lmssystem.service;

import kz.test.lmssystem.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course getCourseByName(String name);

    void updateCourse(Course course);

    void saveCourse(Course course);

    void deleteCourseById(Long id);

}
